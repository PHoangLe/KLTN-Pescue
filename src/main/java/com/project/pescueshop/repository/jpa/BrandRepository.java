package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, String> {
}
