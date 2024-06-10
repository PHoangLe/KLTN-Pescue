package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "liveItem", pluralNoun = "liveItemList")
public class LiveItemRequest {
    private String varietyId;
    private Integer liveStock;
    private Long livePrice;
}
