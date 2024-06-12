package com.project.pescueshop.model.entity.live;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.entity.CartItem;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "LIVE_CART")
@Entity
@Name(prefix = "LICA", noun = "liveCart")
public class LiveCart {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String liveCartId;
    private String sessionId;
    private String userId;
    private String merchantId;
    private String status;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "liveCartId",referencedColumnName = "liveCartId")
    private List<LiveCartItem> liveCartItemList;
}
