package com.project.pescueshop.model.key;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@Component
public class MerchantDocumentItemKey implements Serializable {
    private String merchantId;
    private String documentLink;
}
