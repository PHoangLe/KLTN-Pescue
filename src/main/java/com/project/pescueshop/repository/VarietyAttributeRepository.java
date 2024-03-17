package com.project.pescueshop.repository;

import com.project.pescueshop.model.entity.Product;
import com.project.pescueshop.model.entity.VarietyAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VarietyAttributeRepository extends JpaRepository<VarietyAttribute, String> {
}
