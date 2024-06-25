package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.live.*;
import com.project.pescueshop.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LiveCartDAO extends BaseDAO{
    private final LiveCartRepository liveCartRepository;
    private final LiveCartItemRepository liveCartItemRepository;

    public void saveAndFlushLiveCart(LiveCart liveCart) {
        liveCartRepository.save(liveCart);
    }

    public void saveAndFlushLiveCartItem(LiveCartItem liveCartItem) {
        liveCartItemRepository.save(liveCartItem);
    }

    public Long sumValueOfAllSelectedProductInCart(String liveCartId, String userId){
        String sql =
                " SELECT COALESCE(SUM(ci.total_item_price), 0) " +
                        " FROM live_cart_item lci " +
                        " JOIN live_cart lc on lci.cart_id = lc.cart_id " +
                        " JOIN live_item li on li.live_item_id = lci.live_item_id " +
                        " WHERE (lc.live_cart_id = :p_live_cart_id OR lc.user_id = :p_user_id) " +
                        " AND lci.is_selected = true " +
                        " AND li.current_stock_amount >= lci.quantity ";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_live_cart_id", liveCartId)
                .addValue("p_user_id", userId);

        return jdbcTemplate.queryForObject(sql, parameters, Long.class);
    }

    public void removeSelectedCartItem(String liveCartId) {
        String sql =
                " DELETE " +
                        " FROM live_cart_item lci " +
                        " USING live_cart lc " +
                        " WHERE lci.live_cart_id = lc.live_cart_id " +
                        " AND lc.live_cart_id = :p_live_cart_id " +
                        " AND lci.is_selected = true ";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_live_cart_id", liveCartId);

        jdbcTemplate.update(sql, parameters);
    }

    public LiveCartItem findByLiveItemIdAndLiveCartId(String liveItemId, String liveCartId){
        return liveCartItemRepository.findByLiveItemIdAndLiveCartId(liveItemId, liveCartId).orElse(null);
    }

    public LiveCart findByUserId(String userId, String sessionId) {
        return liveCartRepository.findByUserId(userId, sessionId).orElse(null);
    }

    public LiveCart findByCartId(String liveCartId) {
        return liveCartRepository.findById(liveCartId).orElse(null);
    }

    public LiveCart findByLiveCartIdAndUserId(String liveCartId, String userId) {
        return liveCartRepository.findByLiveCartIdAndUserId(liveCartId, userId).orElse(null);
    }

    public LiveCartItem findLiveCartItemByLiveCartItemId(String LiveCartItemId) {
        return liveCartItemRepository.findById(LiveCartItemId).orElse(null);
    }
}
