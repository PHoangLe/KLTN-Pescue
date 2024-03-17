package com.project.pescueshop.model.key;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@Component
public class InvoiceItemKey implements Serializable {
    private String invoiceId;
    private String varietyId;
}
