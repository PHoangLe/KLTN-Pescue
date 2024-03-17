package com.project.pescueshop.repository;

import com.project.pescueshop.model.entity.Variety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VarietyRepository extends JpaRepository<Variety, String> {
    @Query("SELECT v FROM Variety v WHERE v.productId = ?1 AND v.status = 'ACTIVE'")
    List<Variety> findByProductId(String productId);
}
