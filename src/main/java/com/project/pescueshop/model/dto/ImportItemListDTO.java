package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "importItem", pluralNoun = "itemList")
public class ImportItemListDTO {
    private String varietyId;
    private String name;
    private Long importPrice;
    private Integer quantity;
    private Long totalImportPrice;
    private String image;
    private List<String> attributeName;
}
