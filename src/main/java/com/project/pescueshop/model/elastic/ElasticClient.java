package com.project.pescueshop.model.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
public class ElasticClient {
    @Value("${ELASTIC_URL}")
    private String ELASTIC_URL;
    private ElasticsearchClient esClient;

    @PostConstruct
    private void init() throws UnknownHostException {
        RestClient restClient = RestClient
                .builder(new HttpHost(InetAddress.getByName(ELASTIC_URL), 80))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        esClient = new ElasticsearchClient(transport);
    }

    public ElasticsearchClient get() {
        return esClient;
    }
}