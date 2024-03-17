package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "itemGroup", pluralNoun = "groupList")
public class ImportItemGroupDTO {
    private String productId;
    private String importInvoiceId;
    private String productName;
    private String brandName;
    private String productImage;
    private Long totalImport;
}
