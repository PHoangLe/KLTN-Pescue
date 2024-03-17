package com.project.pescueshop.repository;

import com.project.pescueshop.model.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {
    @Query(value = "SELECT * FROM sub_category WHERE category_id = ?1", nativeQuery = true)
    List<SubCategory> findSubCategoriesByCategoryId(String categoryId);
}
