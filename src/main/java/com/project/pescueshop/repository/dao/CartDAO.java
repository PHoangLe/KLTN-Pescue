package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.dto.CartItemDTO;
import com.project.pescueshop.model.dto.InvoiceItemDTO;
import com.project.pescueshop.model.entity.CartItem;
import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.entity.InvoiceItem;
import com.project.pescueshop.repository.inteface.CartItemRepository;
import com.project.pescueshop.repository.inteface.InvoiceItemRepository;
import com.project.pescueshop.repository.mapper.CartItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CartDAO extends BaseDAO{
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    public List<CartItemDTO> getCartItems(String userId, String cartId){
        String sql = "SELECT * FROM get_cart_items_group_by_merchant(:p_cart_id, :p_user_id);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_user_id", userId)
                .addValue("p_cart_id", cartId);

        return jdbcTemplate.query(sql, parameters, cartItemMapper);
    }

    public CartItem findById(String cartItemId){
        return cartItemRepository.findById(cartItemId).orElse(null);
    }

    public CartItem findByVarietyIdAndCartId(String varietyId, String cartId){
        return cartItemRepository.findByVarietyIdAndCartId(varietyId, cartId).orElse(null);
    }

    public Long sumValueOfAllSelectedProductInCart(String cartId, String userId){
        String sql =
                " SELECT COALESCE(SUM(ci.total_item_price), 0) " +
                " FROM cart_item ci " +
                " JOIN cart c on ci.cart_id = c.cart_id " +
                " JOIN variety v on v.variety_id = ci.variety_id " +
                " WHERE (c.cart_id = :p_cart_id OR c.user_id = :p_user_id) " +
                " AND ci.is_selected = true " +
                " AND v.stock_amount >= ci.quantity ";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_cart_id", cartId)
                .addValue("p_user_id", userId);

        return jdbcTemplate.queryForObject(sql, parameters, Long.class);
    }

    public void removeSelectedCartItem(String cartId) {
        String sql =
                " DELETE " +
                " FROM cart_item ci " +
                " USING cart c " +
                " WHERE ci.cart_id = c.cart_id " +
                " AND c.cart_id = :p_cart_id " +
                " AND ci.is_selected = true ";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_cart_id", cartId);

        jdbcTemplate.update(sql, parameters);
    }

    public void addInvoiceItemsToInvoice(Invoice invoice) {
        String sql = "SELECT * FROM add_invoice_item_to_invoice(:p_user_id, :p_invoice_id);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_user_id", invoice.getUserId())
                .addValue("p_invoice_id", invoice.getInvoiceId());

        jdbcTemplate.update(sql, parameters);
    }

    public void deleteCartItem(CartItem cartItem){
        cartItemRepository.delete(cartItem);
    }
}
