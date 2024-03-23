package com.project.pescueshop.repository.inteface;

import com.project.pescueshop.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
