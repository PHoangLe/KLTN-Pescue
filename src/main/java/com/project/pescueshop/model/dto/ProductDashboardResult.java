package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.entity.Brand;
import com.project.pescueshop.model.entity.Product;
import com.project.pescueshop.model.entity.SubCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "product", pluralNoun = "productList")
public class ProductDashboardResult {
    private String productId;
    private String name;
    private SubCategory subCategory;
    private long price;
    private String petType;
    private Brand brand;
    private String detail;
    private String description;
    private Integer avgRating;
    private String status;
    private List<String> images;
    private Long views;
    private Long buyCount;

    public ProductDashboardResult(Product product, Long views, Long buyCount){
        this.productId = product.getProductId();
        this.name = product.getName();
        this.subCategory = product.getSubCategory();
        this.price = product.getPrice();
        this.petType = product.getPetType();
        this.brand = product.getBrand();
        this.description = product.getDescription();
        this.detail = product.getDetail();
        this.avgRating = product.getAvgRating();
        this.images = product.getImages();
        this.views = views == null ? 0 : views;
        this.buyCount = buyCount == null ? 0 : buyCount;
    }
}
