package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.RecommendationResponse;
import com.project.pescueshop.model.dto.ShippingFeeRequest;
import com.project.pescueshop.model.dto.ShippingFeeResponse;
import com.project.pescueshop.model.entity.Product;
import com.project.pescueshop.repository.dao.ProductDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendService {
    private final ProductDAO productDAO;

    public List<Product> getReccomendProductId(String productId) {
        String url = "https://recommendpescue-production.up.railway.app/recommend-product/" + productId;
        RestTemplate restTemplate = new RestTemplate();

        try{
            ResponseEntity<RecommendationResponse> resp = restTemplate.getForEntity(url, RecommendationResponse.class);
            RecommendationResponse body = resp.getBody();

            return productDAO.getProductByList(body.getListProduct());
        }
        catch(Exception e){
            log.error("Error when calling recommendation API: {}", e.getMessage());
            return null;
        }
    }
}
