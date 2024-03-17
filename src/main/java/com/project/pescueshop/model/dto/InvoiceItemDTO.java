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
    private String invoiceId;
    private Integer quantity;
    private Long total_price;
    private String varietyId;
    private String name;
    private String productId;
    private Long unitPrice;
    private String image;
    private Integer stock_amount;
}
