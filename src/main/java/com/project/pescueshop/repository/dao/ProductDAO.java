package com.project.pescueshop.repository.dao;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.project.pescueshop.model.dto.ProductDashboardResult;
import com.project.pescueshop.model.dto.ProductListDTO;
import com.project.pescueshop.model.elastic.ElasticClient;
import com.project.pescueshop.model.elastic.document.ProductData;
import com.project.pescueshop.model.entity.Product;
import com.project.pescueshop.repository.jpa.ProductRepository;
import com.project.pescueshop.repository.mapper.ListProductMapper;
import com.project.pescueshop.util.Util;
import com.project.pescueshop.util.constant.EnumElasticIndex;
import com.project.pescueshop.util.constant.EnumStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RequiredArgsConstructor
@Repository
@Slf4j
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

    public List<ProductDashboardResult> getMostViewsProducts(Integer n, Long daysAmount, String merchantId){
        n = n == null ? 5 : n;
        daysAmount = daysAmount == null ? 30 : daysAmount;

        Pageable pageable = PageRequest.of(0, n);

        List<Object[]> products = productRepository.getMostViewsProducts(pageable, Util.getCurrentDateMinusDays(daysAmount), merchantId);

        List<ProductDashboardResult> results = new ArrayList<>();

        for (Object[] object : products){
            results.add(new ProductDashboardResult((Product) object[0] , (Long) object[1], null));
        }

        return results;
    }

    public List<ProductDashboardResult> getMostBuyProducts(Integer n, Long daysAmount, String merchantId){
        n = n == null ? 5 : n;
        daysAmount = daysAmount == null ? 30 : daysAmount;

        Pageable pageable = PageRequest.of(0, n);

        List<Object[]> products = productRepository.getMostBuyProduct(pageable, Util.getCurrentDateMinusDays(daysAmount), merchantId);

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
        String sql = "        SELECT p.product_id, " +
                "               (SELECT pi.images FROM product_images pi WHERE p.product_id = pi.product_product_id LIMIT 1) as product_image, " +
                "               p.name, " +
                "               p.description, " +
                "               p.avg_rating, " +
                "               p.price, " +
                "               b.name as brand_name, " +
                "               c.name as category_name, " +
                "               count(*) over() as total_products " +
                "        FROM product p " +
                "        JOIN brand b ON p.brand_id = b.brand_id " +
                "        JOIN sub_category sc ON p.sub_category_id = sc.sub_category_id " +
                "        JOIN category c ON c.category_id = sc.category_id " +
                "        WHERE (:p_brand_id IS NULL or p.brand_id = :p_brand_id) " +
                "        and (:p_category_id IS NULL or sc.category_id = :p_category_id) " +
                "        and (:p_sub_category_id IS NULL or p.sub_category_id = :p_sub_category_id) " +
                "        and (:p_min_price IS NULL or p.price >= :p_min_price) " +
                "        and (:p_max_price IS NULL or p.price <= :p_max_price) " +
                "        and (:p_merchant_id IS NULL or p.merchant_id = :p_merchant_id)" +
                "        and p.status = 'ACTIVE' " +
                "        group by p.product_id, p.name, p.description, p.avg_rating, p.price, b.name, c.name " +
                "        LIMIT COALESCE(:p_page_size, 10000) OFFSET COALESCE((:p_page_number - 1) * :p_page_size, 0); ";

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
        String sql = "DELETE FROM product_images WHERE images IN (:p_images);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_images", productImages);

        jdbcTemplate.update(sql, parameters);
    }

    public List<Product> getProductByList(List<String> listProduct) {
        return productRepository.getProductByList(listProduct);
    }

    public List<Product> getProductsByMerchantId(String merchantId) {
        return productRepository.getProductsByMerchantId(merchantId);
    }

    public void bulkUpdateProductStatus(List<Product> products, EnumStatus status) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Product>> futures = new ArrayList<>();

        for (Product product : products) {
            Callable<Product> task = () -> {
                product.setStatus(status.getValue());
                productRepository.save(product);
                pushOrUpdateProductToElasticSearch(product);
                return product;
            };

            Future<Product> future = executorService.submit(task);
            futures.add(future);
        }

        executorService.shutdown();

        for (Future<Product> future : futures) {
            try {
                Product p = future.get();
                log.info("Updated product status(productId: {}, newStatus: {})", p.getProductId(), p.getStatus());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error while updating product status", e);
            }
        }
    }

    public void pushOrUpdateProductToElasticSearch(Product product) {
        ProductData productData = ProductData.fromProduct(product);

        IndexRequest<ProductData> request = IndexRequest.of(i -> i
                .index(EnumElasticIndex.PRODUCT_DATA.getName())
                .id(productData.getProductId())
                .document(productData));
        try {
            ElasticClient.get().index(request);
            log.info("Push product to elastic search: {}", productData);
        } catch (IOException e) {
            log.error("Error when push product to elastic search", e);
        }
    }
}
