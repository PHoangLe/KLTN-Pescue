package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT p FROM Product p WHERE p.productId = ?1 AND p.status = 'ACTIVE'")
    Optional<Product> findByProductId(String id);

    @Query(value = "SELECT * FROM Product p WHERE p.brand_id = ?1 AND p.status = 'ACTIVE'", nativeQuery = true)
    List<Product> getProductByBrandId(String brandId);

    @Query(value = "SELECT * FROM Product p WHERE p.category_id = ?1 AND p.status = 'ACTIVE'", nativeQuery = true)
    List<Product> getProductByCategoryId(String categoryId);

    @Query(value = "SELECT p, COUNT(val.viewAuditLogId) AS views " +
            "FROM Product p " +
            "JOIN ViewAuditLog val ON p.productId = val.objectId " +
            "WHERE val.objectType = 'PRODUCT' " +
            "AND val.date >= :afterDate " +
            "and (p.merchantId = :merchantId or :merchantId is null) " +
            "GROUP BY p.productId " +
            "ORDER BY COUNT(val.viewAuditLogId) DESC")
    List<Object[]> getMostViewsProducts(Pageable pageable, @Param("afterDate") Date afterDate, @Param("merchantId") String merchantId);


    @Query(
            value = "select p, sum(ii.quantity) " +
                    "from Product p " +
                    "join Variety v on p.productId = v.productId " +
                    "join InvoiceItem ii on ii.varietyId = v.varietyId " +
                    "join Invoice i on i.invoiceId = ii.invoiceId " +
                    "where i.createdDate >= :afterDate " +
                    "and (i.merchantId = :merchantId or :merchantId is null) " +
                    "group by p.productId " +
                    "order by sum(ii.quantity) desc "
    )
    List<Object[]> getMostBuyProduct(Pageable pageable, @Param("afterDate") Date afterDate, @Param("merchantId") String merchantId);

    @Query(value = "SELECT p" +
            "    FROM Product p" +
            "    ORDER BY RANDOM() ")
    List<Product> getRandomNProduct(Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.productId IN :listProduct")
    List<Product> getProductByList(List<String> listProduct);

    @Query(value = "SELECT p FROM Product p WHERE p.merchantId = :merchantId")
    List<Product> getProductsByMerchantId(String merchantId);
}
