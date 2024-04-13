package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.entity.Brand;
import com.project.pescueshop.model.entity.SubCategory;
import com.project.pescueshop.model.entity.VarietyAttribute;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "product", pluralNoun = "productList")
public class ProductListDTO {
    private String productId;
    private String name;
    private long price;
    private String image;
    private String description;
    private Integer avgRating;
}
