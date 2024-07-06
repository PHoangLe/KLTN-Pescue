package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.dto.ProductDashboardResult;
import com.project.pescueshop.model.dto.ProductListDTO;
import com.project.pescueshop.model.entity.Product;
import com.project.pescueshop.repository.jpa.ProductRepository;
import com.project.pescueshop.repository.mapper.ListProductMapper;
import com.project.pescueshop.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductDAO extends BaseDAO{
    private final ProductRepository productRepository;
    private final ListProductMapper listProductMapper;

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

    public List<ProductListDTO> getListProduct(String categoryId, String subCategoryId, String brandId, String merchantId, Long minPrice, Long maxPrice, Integer page, Integer size){
        String sql = "SELECT * FROM get_products(:p_brand_id, :p_category_id, :p_sub_category_id, :p_min_price, :p_max_price, :p_merchant_id, :p_page_number, :p_page_size);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_brand_id", brandId, Types.VARCHAR)
                .addValue("p_category_id", categoryId, Types.VARCHAR)
                .addValue("p_sub_category_id", subCategoryId, Types.VARCHAR)
                .addValue("p_min_price", minPrice, Types.BIGINT)
                .addValue("p_max_price", maxPrice, Types.BIGINT)
                .addValue("p_merchant_id", merchantId, Types.VARCHAR)
                .addValue("p_page_number", page, Types.INTEGER)
                .addValue("p_page_size", size, Types.INTEGER);

        return jdbcTemplate.query(sql, parameters, listProductMapper);
    }

    public void addNewProductImages(String productId, List<String> productImages) {
        String sql = "INSERT INTO product_images (product_product_id, images) VALUES (:p_product_id, :p_images);";

         for (String image : productImages){
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("p_product_id", productId)
                    .addValue("p_images", image);

            jdbcTemplate.update(sql, parameters);
        }
    }

    public void removeProductImages(List<String> productImages) {
        String sql = "DELETE FROM products_images WHERE images IN (:p_images);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_images", productImages);

        jdbcTemplate.update(sql, parameters);
    }
}
