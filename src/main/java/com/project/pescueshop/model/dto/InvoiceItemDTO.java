package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "invoiceItem", pluralNoun = "itemList")
public class InvoiceItemDTO {
    private String userId;
    private String merchantUserId;
    private String invoiceId;
    private String merchantId;
    private Integer quantity;
    private Long totalPrice;
    private String varietyId;
    private String name;
    private String productId;
    private Long unitPrice;
    private String image;
    private Integer stockAmount;
    private Integer weight;
    private Integer height;
    private Integer length;
    private Integer width;
}
