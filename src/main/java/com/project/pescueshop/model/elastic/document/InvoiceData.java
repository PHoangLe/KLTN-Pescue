package com.project.pescueshop.model.elastic.document;

import lombok.*;

@Getter
@Setter
@Builder
public class InvoiceData {
    private String invoiceId;
    private String productId;
    private Integer avgRating;
    private String categoryId;
    private String userId;
}