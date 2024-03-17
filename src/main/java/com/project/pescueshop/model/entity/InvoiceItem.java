package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.key.InvoiceItemKey;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "INVOICE_ITEM")
@Entity
@Builder
@AllArgsConstructor
@Name(prefix = "IVIT")
@IdClass(InvoiceItemKey.class)
public class InvoiceItem {
    @Id
    private String invoiceId;
    @Id
    private String varietyId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "varietyId", referencedColumnName = "varietyId", insertable = false, updatable = false)
    private Variety variety;
    private Integer quantity;
    private Long totalPrice;
}
