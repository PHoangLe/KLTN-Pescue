package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "merchant", pluralNoun = "merchantList")
public class MerchantDTO {
    private String merchantId;
    private String userId;
    private String merchantName;
    private String merchantDescription;
    private Date createdDate;
    private String merchantAvatar;
    private String merchantCover;
    private Integer rating;
    private String location;
    private String phoneNumber;
    private Integer noProduct;
    private List<String> relatedDocuments;
    private Boolean isLiveable;
    private Boolean isApproved;
    private Boolean isSuspended;
}
