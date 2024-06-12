package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.live.LiveCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LiveCartItemRepository extends JpaRepository<LiveCartItem, String> {
    @Query(value = "SELECT * FROM live_cart_item lci WHERE lci.live_item_id = ?1 AND lci.live_cart_id = ?2", nativeQuery = true)
    Optional<LiveCartItem> findByLiveItemIdAndLiveCartId(String liveItemId, String liveCartId);
}
