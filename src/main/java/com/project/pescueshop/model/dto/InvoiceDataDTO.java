package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "data", pluralNoun = "data")
public class InvoiceDataDTO {
    private String invoiceId;
    private String productId;
    private Integer avgRating;
    private String categoryId;
    private String userId;
}
