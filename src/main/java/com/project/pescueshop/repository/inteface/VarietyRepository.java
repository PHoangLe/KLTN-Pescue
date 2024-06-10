package com.project.pescueshop.repository.inteface;

import com.project.pescueshop.model.entity.Variety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VarietyRepository extends JpaRepository<Variety, String> {
    @Query("SELECT v FROM Variety v WHERE v.productId = ?1 AND v.status = 'ACTIVE'")
    List<Variety> findByProductId(String productId);

    @Query(value = "SELECT pi.images " +
            "FROM product_images pi " +
            "JOIN product p ON p.product_id = pi.product_id " +
            "JOIN variety v ON v.product_id = p.product_id " +
            "WHERE v.variety_id = ?1 LIMIT 1", nativeQuery = true)
    String getCoverImageById(String varietyId);
}
