package org.example.controller;

import co.elastic.clients.elasticsearch.core.search.Hit;
import org.example.model.Book;
import org.example.model.JsonResponse;
import org.example.model.Video;
import org.example.service.ElasticsearchSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ElasticsearchSearchService searchService;

    @GetMapping("/by-author")
    public JsonResponse<List<Book>> searchByAuthor(@RequestParam String author) {
        try {
            List<Hit<Book>> hits = searchService.searchByAuthor(author);
            if (hits == null || hits.isEmpty()) {
                return new JsonResponse<>(new ArrayList<>());
            }
            List<Book> books = hits.stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
            return new JsonResponse<>(books);
        } catch (Exception e) {
            log.warn("[es/search] failed: {}", e.getMessage());
            return new JsonResponse<>(new ArrayList<>());
        }
    }

    @GetMapping("/by-author-with-and")
    public JsonResponse<List<Book>> searchByAuthorWithAnd(@RequestParam String author) {
        try {
            List<Hit<Book>> hits = searchService.searchByAuthorWithAnd(author);
            if (hits == null || hits.isEmpty()) {
                return new JsonResponse<>(new ArrayList<>());
            }
            List<Book> books = hits.stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
            return new JsonResponse<>(books);
        } catch (Exception e) {
            log.warn("[es/search] failed: {}", e.getMessage());
            return new JsonResponse<>(new ArrayList<>());
        }
    }

    @GetMapping("/complex-bool-search")
    public JsonResponse<List<Book>> complexBoolSearch(
            @RequestParam String author,
            @RequestParam Double rating,
            @RequestParam String releaseDate,
            @RequestParam String tags,
            @RequestParam Integer edition) {
        try {
            List<Hit<Book>> hits = searchService.complexBoolSearch(author, rating, releaseDate, tags, edition);
            if (hits == null || hits.isEmpty()) {
                return new JsonResponse<>(new ArrayList<>());
            }
            List<Book> books = hits.stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
            return new JsonResponse<>(books);
        } catch (Exception e) {
            log.warn("[es/search] failed: {}", e.getMessage());
            return new JsonResponse<>(new ArrayList<>());
        }
    }

    @GetMapping("/find-videos")
    public JsonResponse<List<Video>> findVideos(
            @RequestParam String keyword,
            @RequestParam Long pubtime_begin_s,
            @RequestParam Long pubtime_end_s,
            @RequestParam String order,
            @RequestParam Integer duration,
            @RequestParam Integer limit,
            @RequestParam Integer offset) {
        try {
            List<Hit<Video>> hits = searchService.findVideos(keyword, pubtime_begin_s, pubtime_end_s, order, duration, limit, offset);
            if (hits == null || hits.isEmpty()) {
                return new JsonResponse<>(new ArrayList<>());
            }
            List<Video> videos = hits.stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
            return new JsonResponse<>(videos);
        } catch (Exception e) {
            log.warn("[es/search] failed: {}", e.getMessage());
            return new JsonResponse<>(new ArrayList<>());
        }
    }
}
