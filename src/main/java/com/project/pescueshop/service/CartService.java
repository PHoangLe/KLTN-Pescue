package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.CartDTO;
import com.project.pescueshop.model.dto.CartItemDTO;
import com.project.pescueshop.model.dto.AddOrUpdateCartItemDTO;
import com.project.pescueshop.model.dto.MerchantGroupCartItem;
import com.project.pescueshop.model.entity.*;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.jpa.CartItemRepository;
import com.project.pescueshop.repository.jpa.CartRepository;
import com.project.pescueshop.repository.dao.CartDAO;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService{
    private final CartRepository cartRepository;
    private final CartDAO cartDAO;
    private final CartItemRepository cartItemRepository;
    private final VarietyService varietyService;
    private final UserService userService;

    public String createCartForNewUser(String userId){
        Cart newCart = Cart.builder()
                .userId(userId)
                .build();

        cartRepository.saveAndFlush(newCart);
        return newCart.getCartId();
    }

    public Cart findCartByUserId(String userId){
        return cartRepository.getCartByUserId(userId);
    }

    public Cart findCartByCartId(String cartId){
        return cartRepository.findById(cartId).orElse(null);
    }

    public List<MerchantGroupCartItem> getCartItemByUserId(String userId){
        return getCartItems(userId, null);
    }

    public List<MerchantGroupCartItem> getCartItemByCartId(String cartId) {
        return getCartItems(null, cartId);
    }

    public List<MerchantGroupCartItem> getCartItems(String userId, String cartId){
        List<CartItemDTO> cartItems = cartDAO.getCartItems(userId, cartId);

        return cartItems.stream()
                .collect(Collectors.groupingBy(CartItemDTO::getMerchantId))
                .entrySet()
                .stream()
                .map(entry -> MerchantGroupCartItem.builder()
                        .merchantId(entry.getKey())
                        .merchantName(entry.getValue().get(0).getMerchantName())
                        .merchantAvatar(entry.getValue().get(0).getMerchantAvatar())
                        .cityName(entry.getValue().get(0).getCityName())
                        .districtName(entry.getValue().get(0).getDistrictName())
                        .wardName(entry.getValue().get(0).getWardName())
                        .cartItemDTOList(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public void addOrUpdateCartItem(AddOrUpdateCartItemDTO dto, User user, Cart existedCart) throws FriendlyException {
        Variety variety = varietyService.findById(dto.getVarietyId());

        if (variety == null){
            throw new FriendlyException(EnumResponseCode.VARIETY_NOT_FOUND);
        }

        Cart cart = existedCart != null ? existedCart : findCartByUserId(user.getUserId());
        if (cart == null){
            throw new FriendlyException(EnumResponseCode.CART_NOT_FOUND);
        }

        CartItem cartItem = cartDAO.findByVarietyIdAndCartId(dto.getVarietyId(), cart.getCartId());
        int newQuantity = cartItem == null ? dto.getQuantity() : cartItem.getQuantity() + dto.getQuantity();
        if (cartItem != null && newQuantity == 0){
            cart.getCartItemList().remove(cartItem);
            cartRepository.saveAndFlush(cart);
            return;
        } else if (newQuantity < 0) {
            return;
        }

        if (cartItem == null) {
            cartItem = new CartItem();
        }
        cartItem.setCartId(cart.getCartId());
        cartItem.setProduct(variety);
        cartItem.setSelected(true);
        cartItem.setQuantity(newQuantity);
        cartItem.setTotalItemPrice(newQuantity * variety.getPrice());

        cartItemRepository.saveAndFlush(cartItem);
    }

    public void selectCartItem(String cartItemId) throws FriendlyException {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);

        if (cartItem == null){
            throw new FriendlyException(EnumResponseCode.CART_ITEM_NOT_FOUND);
        }

        cartItem.setSelected(!cartItem.isSelected());

        cartItemRepository.saveAndFlush(cartItem);
    }

    public CartDTO getUnAuthenticatedCart(String cartId) {
        if (cartId != null) {
            List<MerchantGroupCartItem> cartItemDTOS = getCartItemByCartId(cartId);

            CartDTO cartDTO = CartDTO.builder()
                    .cartItemList(cartItemDTOS)
                    .build();

            if (!CollectionUtils.isEmpty(cartItemDTOS)){
                cartDTO.setCartId(cartItemDTOS.get(0).getCartItemDTOList().get(0).getCartId());
            }

            return cartDTO;
        }

        User user = userService.getAdminUser();
        String newCartId = createCartForNewUser(user.getUserId());

        return CartDTO.builder()
                .cartId(newCartId)
                .cartItemList(new ArrayList<>())
                .build();
    }

    public void addOrUpdateUnAuthenticatedCartItem(AddOrUpdateCartItemDTO dto, String cartId) throws FriendlyException {
        Cart cart = findCartByCartId(cartId);
        if (cart == null){
            throw new FriendlyException(EnumResponseCode.CART_NOT_FOUND);
        }

        User user = userService.getAdminUser();

        addOrUpdateCartItem(dto, user, cart);
    }
}
