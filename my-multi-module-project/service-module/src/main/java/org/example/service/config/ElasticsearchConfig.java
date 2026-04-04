package org.example.service.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.url}")
    private String esUrl;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // 配置基本认证
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("", ""));

        // 创建RestClient
        RestClient restClient = RestClient.builder(HttpHost.create(esUrl))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    // 设置连接池大小，适用于高并发场景
                    httpClientBuilder.setMaxConnTotal(500);
                    httpClientBuilder.setMaxConnPerRoute(300);
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }).build();

        // 创建传输层
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // 创建Elasticsearch客户端
        return new ElasticsearchClient(transport);
    }
}
