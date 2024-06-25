package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "data", pluralNoun = "datas")
public class InvoiceDataDTO {
    private String invoiceId;
    private String userId;
    private String productId;
}
