package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "voucherInput", pluralNoun = "voucherList")
public class VoucherInputDTO {
    private String type;
    private long value;
    private long price;
    private long maxValue;
    private long minInvoiceValue;
}
