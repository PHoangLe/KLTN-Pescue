package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "importItem", pluralNoun = "itemList")
public class AddOrUpdateImportItemDTO {
    private String importInvoiceId;
    private String varietyId;
    private Integer quantity;
    private Long importPrice;
}
