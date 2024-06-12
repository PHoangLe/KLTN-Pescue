package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @Query("SELECT c FROM Cart c WHERE c.userId = ?1")
    Cart getCartByUserId(String userId);
}
