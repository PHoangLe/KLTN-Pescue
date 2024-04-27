package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.entity.*;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "product", pluralNoun = "productList")
public class ProductDTO {
    private String productId;
    private String name;
    private String merchantId;
    private String subCategoryId;
    private SubCategory subCategory;
    private long price;
    private int petTypeId;
    private String petType;
    private String brandId;
    private List<String> images;
    private Brand brand;
    private String detail;
    private String description;
    private Integer avgRating;
    private List<VarietyDTO> varieties;
    private String status;
    private Integer width;
    private Integer height;
    private Integer length;
    private Integer weight;
    private List<VarietyAttribute> varietyAttributeList;
    private List<RatingResultDTO> ratingList;

    public ProductDTO(Product product){
        this.productId = product.getProductId();
        this.merchantId = product.getMerchantId();
        this.name = product.getName();
        this.subCategory = product.getSubCategory();
        this.price = product.getPrice();
        this.petType = product.getPetType();
        this.brand = product.getBrand();
        this.detail = product.getDetail();
        this.description = product.getDescription();
        this.width = product.getWidth();
        this.height = product.getHeight();
        this.length = product.getLength();
        this.weight = product.getWeight();
        this.avgRating = product.getAvgRating();
        this.images = product.getImages();
        this.status = product.getStatus();
    }
}
