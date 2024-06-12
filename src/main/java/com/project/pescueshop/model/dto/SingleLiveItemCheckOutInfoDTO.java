package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.Embedded;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "liveItemCheckOutInfo")
public class SingleLiveItemCheckOutInfoDTO {
    @Embedded
    private PaymentInfoDTO paymentInfoDTO;
    private String liveItemId;
    private Integer quantity;
}

