package com.project.pescueshop.model.entity.live;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.key.LiveInvoiceItemKey;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "LIVE_INVOICE_ITEM")
@Entity
@Name(prefix = "LIIV", noun = "liveInvoiceItem")
@IdClass(LiveInvoiceItemKey.class)
public class LiveInvoiceItem {
    @Id
    private String liveInvoiceId;
    @Id
    private String liveItemId;
    private String sessionId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "liveItemId", referencedColumnName = "liveItemId", insertable = false, updatable = false)
    private LiveItem liveItem;
    private Integer quantity;
    private Long totalPrice;
}
