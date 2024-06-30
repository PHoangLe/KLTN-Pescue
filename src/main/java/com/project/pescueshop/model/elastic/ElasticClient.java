package com.project.pescueshop.model.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticClient {
    private static ElasticsearchClient esClient;

    @PostConstruct
    private static void init() {
        String url = System.getenv("ELASTIC_URL");
        RestClient restClient = RestClient
                .builder(HttpHost.create(url))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        esClient = new  ElasticsearchClient(transport);
    }

    public static ElasticsearchClient get() {
        return esClient;
    }
}