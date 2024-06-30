package com.project.pescueshop.model.elastic.document;

import com.project.pescueshop.model.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

@Getter
@Setter
@Builder
public class ProductData {
    private String productId;
    private String image;
    private String merchantId;
    private String categoryId;
    private String categoryName;
    private String subCategoryId;
    private String subCategoryName;
    private String name;
    private long price;
    private String petType;
    private String brandId;
    private String brandName;
    private String detail;
    private String description;
    private Integer avgRating;
    private String status;

    public static ProductData fromProduct(Product product) {
        String imgURL = ArrayUtils.isEmpty(product.getImages().toArray()) ? "" : product.getImages().get(0);

        return ProductData.builder()
                .image(imgURL)
                .productId(product.getProductId())
                .merchantId(product.getMerchantId())
                .categoryId(product.getSubCategory().getCategory().getCategoryId())
                .categoryName(product.getSubCategory().getCategory().getName())
                .subCategoryId(product.getSubCategory().getSubCategoryId())
                .subCategoryName(product.getSubCategory().getName())
                .name(product.getName())
                .price(product.getPrice())
                .petType(product.getPetType())
                .brandId(product.getBrand().getBrandId())
                .brandName(product.getBrand().getName())
                .detail(product.getDetail())
                .description(product.getDescription())
                .avgRating(product.getAvgRating())
                .status(product.getStatus())
                .build();
    }
}
