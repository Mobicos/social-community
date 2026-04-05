package org.example.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.util.ObjectBuilder;
import org.example.model.Book;
import org.example.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class ElasticsearchSearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    /**
     * 匹配查询：查询特定作者撰写的书
     */
    public List<Hit<Book>> searchByAuthor(String author) throws IOException {
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .match(m -> m
                                .field("author")
                                .query(author)
                        )
                )
        );

        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);
        return response.hits().hits();
    }

    /**
     * 匹配查询：查询特定作者撰写的书，使用AND操作符
     */
    public List<Hit<Book>> searchByAuthorWithAnd(String author) throws IOException {
        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .match(m -> m
                                .field("author")
                                .query(author)
                                .operator(Operator.And)
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    /**
     * 多字段匹配查询：在多个字段中搜索特定关键词
     */
    public List<Hit<Book>> multiMatchSearch(String query, List<String> fields) throws IOException {
        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .multiMatch(m -> m
                                .query(query)
                                .fields(fields)
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    /**
     * 多字段匹配查询：在多个字段中搜索特定关键词，并对指定字段设置提升因子
     */
    public List<Hit<Book>> multiMatchSearchWithBoost(String query, List<String> fields) throws IOException {
        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .multiMatch(m -> m
                                .query(query)
                                .fields(fields)
                                .boost(Float.valueOf(3)) // 提升因子
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    /**
     * 短语匹配查询：在指定字段中搜索包含精确短语的书籍
     */
    public List<Hit<Book>> matchPhraseSearch(String field, String phrase) throws IOException {
        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .matchPhrase(mp -> mp
                                .field(field)
                                .query(phrase)
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    /**
     * 短语匹配查询：在指定字段中搜索包含精确短语的书籍，并高亮显示匹配的短语
     */
    public List<Hit<Book>> matchPhraseSearchWithHighlight(String field, String phrase) throws IOException {
        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .matchPhrase(mp -> mp
                                .field(field)
                                .query(phrase)
                        )
                )
                .highlight(h -> h
                        .fields(field, f -> f
                                .preTags("<em>")
                                .postTags("</em>")
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    /**
     * 短语匹配查询：在指定字段中搜索包含精确短语的书籍，处理缺失单词的短语
     */
    public List<Hit<Book>> matchPhraseSearchWithSlop(String field, String phrase, int slop) throws IOException {
        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .matchPhrase(mp -> mp
                                .field(field)
                                .query(phrase)
                                .slop(slop)
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    /**
     * 模糊匹配查询：在指定字段中搜索包含特定关键词的书籍，并允许一定的拼写错误
     */
    public List<Hit<Book>> fuzzySearch(String field, String query, String fuzziness) throws IOException {
        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .match(m -> m
                                .field(field)
                                .query(query)
                                .fuzziness(fuzziness)
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    /**
     * 词项级查询：在指定字段中搜索特定值，并返回指定的源字段
     */
    public List<Hit<Book>> termSearchWithSource(String field, int value, List<String> sourceFields) throws IOException {
        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .source(SourceConfig.of((Function<SourceConfig.Builder, ObjectBuilder<SourceConfig>>) sourceFields))
                .query(q -> q
                        .term(t -> t
                                .field(field)
                                .value(value)
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    /**
     * 布尔查询：执行多个必须匹配的条件
     */
    public List<Hit<Book>> boolSearchWithMustConditions(String authorField, String authorValue, String synopsisField, String synopsisValue) throws IOException {
        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .bool(b -> b
                                .must(m1 -> m1
                                        .match(m -> m
                                                .field(authorField)
                                                .query(authorValue)
                                        )
                                )
                                .must(m2 -> m2
                                        .matchPhrase(mp -> mp
                                                .field(synopsisField)
                                                .query(synopsisValue)
                                        )
                                )
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    /**
     * 布尔查询：执行必须匹配和必须不匹配的条件
     */
    public List<Hit<Book>> boolSearchWithMustAndMustNot(String authorField, String authorValue, String ratingField, double ratingValue) throws IOException {
        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .match(mat -> mat
                                                .field(authorField)
                                                .query(authorValue)
                                        )
                                )
                                .mustNot(mn -> mn
                                        .range(rq -> rq
                                                .number(n -> n
                                                        .field(ratingField)
                                                        .lt(ratingValue))
                                        )
                                )
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    /**
     * 布尔查询：执行复杂的查询条件，包括 must、must_not、should 和 filter
     */
    public List<Hit<Book>> complexBoolSearch(String author, double rating, String releaseDate, String tags, int edition) throws IOException {
        // Search by author
        Query byAuthor = MatchQuery.of(m -> m
                .field("author")
                .query(author)
        )._toQuery();

        // Search by rating
        Query byRating = RangeQuery.of(r -> r
                .number(n -> n
                        .field("amazon_rating")
                        .lt(rating))
        )._toQuery();

        // Search by tags
        Query byTags = MatchQuery.of(m -> m
                .field("tags")
                .query(tags)
        )._toQuery();

        List<Query> filterConditions = new ArrayList<>();

        // Search by release date
        Query byReleaseDate = RangeQuery.of(r -> r
                .date(n -> n
                        .field("release_date")
                        .gte(releaseDate))
        )._toQuery();
        filterConditions.add(byReleaseDate);

        Query byEdition = TermQuery.of(t -> t
                .field("edition")
                .value(edition)
        )._toQuery();
        filterConditions.add(byEdition);

        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("books")
                .query(q -> q
                        .bool(b -> b
                                .must(byAuthor)
                                .mustNot(byRating)
                                .should(byTags)
                                .filter(filterConditions)
                        )
                )
        );

        // 执行搜索
        SearchResponse<Book> response = elasticsearchClient.search(request, Book.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    public List<Hit<Video>> findVideos(String keyword, Long pubtime_begin_s, Long pubtime_end_s, String order, Integer duration, Integer limit,
                                      Integer offset) throws IOException {
        // Search by title
        Query byTitle = MatchQuery.of(m -> m
                .field("title")
                .query(keyword)
        )._toQuery();

        List<Query> filterConditions = new ArrayList<>();

        // Search by publish_date
        Query byPublishDate = RangeQuery.of(r -> r
                .date(n -> n
                        .field("publish_date")
                        .gte(convertTimestampToDate(pubtime_begin_s))
                        .lte(convertTimestampToDate(pubtime_end_s))
                )
        )._toQuery();
        filterConditions.add(byPublishDate);

        Query byDuration = TermQuery.of(t -> t
                .field("duration")
                .value(duration)
        )._toQuery();
        filterConditions.add(byDuration);

        // 构建搜索请求
        SearchRequest request = SearchRequest.of(builder -> builder
                .index("videos")
                .size(limit) // how many results to return
                .from(offset) // starting point
                .query(q -> q
                        .bool(b -> b
                                .must(byTitle)
                                .filter(filterConditions)
                        )
                )
                .sort(srt -> srt
                        .field(fld -> fld
                                .field(order)
                                .order(SortOrder.Desc)))
        );
        System.out.println(request);

        // 执行搜索
        SearchResponse<Video> response = elasticsearchClient.search(request, Video.class);

        // 返回搜索结果
        return response.hits().hits();
    }

    public static String convertTimestampToDate(long timestamp) {

        // 将 Unix 时间戳转换为 LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp),
                ZoneId.systemDefault() // 使用系统默认时区
        );

        // 提取日期部分并转换为 yyyy-MM-dd 格式的字符串
        String dateTimeStr = dateTime.toLocalDate().toString();
        return dateTimeStr;
    }

}
