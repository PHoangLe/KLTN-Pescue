package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "createMerchantRequest", pluralNoun = "createMerchantRequests")
@Data
@Builder
public class CreateMerchantRequest {
    private String userId;
    private String merchantName;
    private String merchantDescription;
    private String location;
    private String phoneNumber;
}
