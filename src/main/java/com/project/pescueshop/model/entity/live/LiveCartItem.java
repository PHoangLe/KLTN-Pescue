package com.project.pescueshop.model.entity.live;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "LIVE_CART_ITEM")
@Entity
@Name(prefix = "LICI", noun = "liveCartItem")
public class LiveCartItem {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String liveCartItemId;
    private String liveCartId;
    @Transient
    private String liveItemId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "liveItemId", referencedColumnName = "liveItemId")
    private LiveItem liveItem;
    private Integer quantity;
    private Long totalItemPrice;
    private Boolean isSelected;
}
