package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CART_ITEM")
@Entity
@Name(prefix = "CAIT", noun = "cartItem", pluralNoun = "cartItemList")
public class CartItem {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String cartItemId;
    private String cartId;
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "varietyId", referencedColumnName = "varietyId")
    private Variety product;
    private Integer quantity;
    private long totalItemPrice;
    private boolean isSelected;
}
