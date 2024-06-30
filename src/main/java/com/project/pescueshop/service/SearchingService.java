package com.project.pescueshop.service;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.pescueshop.model.dto.GlobalSearchResultDTO;
import com.project.pescueshop.model.elastic.ElasticClient;
import com.project.pescueshop.model.elastic.document.MerchantData;
import com.project.pescueshop.model.elastic.document.ProductData;
import com.project.pescueshop.repository.dao.SearchingDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.concurrent.CompletedFuture;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchingService {

    public Map<String, List<GlobalSearchResultDTO>> globalSearch(String keyword, Integer size) throws ExecutionException, InterruptedException {
        CompletableFuture<List<MerchantData>> merchantDataFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return ElasticClient.get().search(s -> s
                        .index("merchant_data")
                        .size(size)
                        .query(q -> q
                                .queryString(qs -> qs.query(keyword))
                        ), MerchantData.class).hits().hits().stream()
                        .map(Hit::source)
                        .toList();
            } catch (IOException e) {
                log.error("Error while searching merchant data", e);
                return List.of();
            }
        });

        CompletableFuture<List<ProductData>> productDataFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return ElasticClient.get().search(s -> s
                        .index("product_data")
                        .size(size)
                        .query(q -> q
                                .queryString(qs -> qs.query(keyword))
                        ), ProductData.class).hits().hits().stream()
                        .map(Hit::source)
                        .toList();
            } catch (IOException e) {
                log.error("Error while searching product data", e);
                return List.of();
            }
        });

        List<MerchantData> merchantDataList = merchantDataFuture.get();
        List<ProductData> productDataList = productDataFuture.get();

        Map<String, List<GlobalSearchResultDTO>> resultList = new HashMap<>();
        resultList.put("merchant", merchantDataList.stream()
                .map(merchantData -> GlobalSearchResultDTO.builder()
                        .groupName("merchant")
                        .itemId(merchantData.getMerchantId())
                        .itemImage(merchantData.getMerchantAvatar())
                        .itemName(merchantData.getMerchantName())
                        .build())
                .collect(Collectors.toList()));

        resultList.put("product", productDataList.stream()
                .map(productData -> GlobalSearchResultDTO.builder()
                        .groupName("product")
                        .itemId(productData.getProductId())
                        .itemImage(productData.getImage())
                        .itemName(productData.getName())
                        .build())
                .collect(Collectors.toList()));

        return resultList;
    }
}
