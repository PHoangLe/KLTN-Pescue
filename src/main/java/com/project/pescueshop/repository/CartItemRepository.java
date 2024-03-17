package com.project.pescueshop.repository;

import com.project.pescueshop.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    @Query(value = "SELECT * FROM cart_item ci WHERE ci.variety_id = ?1 AND ci.cart_id = ?2", nativeQuery = true)
    Optional<CartItem> findByVarietyIdAndCartId(String varietyId, String cartId);
}
