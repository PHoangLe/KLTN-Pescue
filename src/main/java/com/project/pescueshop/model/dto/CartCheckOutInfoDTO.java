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
@Name(noun = "cartCheckOutInfo")
public class CartCheckOutInfoDTO {
    @Embedded
    private PaymentInfoDTO paymentInfoDTO;
    private String cartId;
}
