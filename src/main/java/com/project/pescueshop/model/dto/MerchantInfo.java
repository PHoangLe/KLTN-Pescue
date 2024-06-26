package com.project.pescueshop.model.dto;


import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "merchant", pluralNoun = "merchantList")
public class MerchantInfo {
    private String merchantAvatar;
    private String merchantName;
    private String userId;
}
