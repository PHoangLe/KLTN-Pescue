package com.project.pescueshop.model.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ElasticClient {
    @Value("${ELASTIC_URL}")
    private String ELASTIC_URL;
    private final ElasticsearchClient esClient;

    private ElasticClient(){
        RestClient restClient = RestClient
                .builder(HttpHost.create(ELASTIC_URL))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        this.esClient = new ElasticsearchClient(transport);
    }

    public ElasticsearchClient get() {
        if (this.esClient == null){
            new ElasticClient();
        }

        return this.esClient;
    }
}
