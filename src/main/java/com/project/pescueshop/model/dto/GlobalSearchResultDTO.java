package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "searchResult", pluralNoun = "searchResultList")@Data
@Builder
public class GlobalSearchResultDTO {
    private String groupName;
    private String itemName;
    private String itemId;
    private String itemImage;
}
