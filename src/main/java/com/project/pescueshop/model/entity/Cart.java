package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "CART")
@Entity
@Name(prefix = "CART", noun = "cart", pluralNoun = "cartList")
@Builder
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String cartId;
    private String userId;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "cartId",referencedColumnName = "cartId")
    private List<CartItem> cartItemList;
}
