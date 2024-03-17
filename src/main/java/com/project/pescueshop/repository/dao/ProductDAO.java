package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.dto.ProductDashboardResult;
import com.project.pescueshop.model.entity.Product;
import com.project.pescueshop.repository.ProductRepository;
import com.project.pescueshop.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductDAO extends BaseDAO{
    private final ProductRepository productRepository;

    public void deleteAttribute(String productId, String attributeId){
        String sql = "SELECT delete_product_attribute(:p_productId, :p_attributeId);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_productId", productId)
                .addValue("p_attributeId", attributeId);

        jdbcTemplate.update(sql, parameters);
    }

    public String getProductImage(String productId){
        String sql = "SELECT images FROM products_images WHERE product_product_id = :p_productId";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_productId", productId);

        return jdbcTemplate.queryForObject(sql, parameters, String.class);
    }

    public List<Product> getRandomNProduct(Integer n){
        n = n == null ? 5 : n;

        Pageable pageable = PageRequest.of(0, n);
        return productRepository.getRandomNProduct(pageable);
    }

    public List<ProductDashboardResult> getMostViewsProducts(Integer n, Long daysAmount){
        n = n == null ? 5 : n;
        daysAmount = daysAmount == null ? 30 : daysAmount;

        Pageable pageable = PageRequest.of(0, n);

        List<Object[]> products = productRepository.getMostViewsProducts(pageable, Util.getCurrentDateMinusDays(daysAmount));

        List<ProductDashboardResult> results = new ArrayList<>();

        for (Object[] object : products){
            results.add(new ProductDashboardResult((Product) object[0] , (Long) object[1], null));
        }

        return results;
    }

    public List<ProductDashboardResult> getMostBuyProducts(Integer n, Long daysAmount){
        n = n == null ? 5 : n;
        daysAmount = daysAmount == null ? 30 : daysAmount;

        Pageable pageable = PageRequest.of(0, n);

        List<Object[]> products = productRepository.getMostBuyProduct(pageable, Util.getCurrentDateMinusDays(daysAmount));

        List<ProductDashboardResult> results = new ArrayList<>();

        for (Object[] object : products){
            results.add(new ProductDashboardResult((Product) object[0] , null, (Long) object[1]));
        }

        return results;
    }

    public List<Product> getProductByBrandId(String brandId) {
        return productRepository.getProductByBrandId(brandId);
    }

    public List<Product> getProductByCategoryId(String categoryId) {
        return productRepository.getProductByCategoryId(categoryId);
    }

    public void saveAndFlushProduct(Product product){
        productRepository.saveAndFlush(product);
    }

    public Product findProductById(String id){
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
