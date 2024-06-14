package com.project.pescueshop.service.live;

import com.project.pescueshop.model.dto.AddOrUpdateLiveCartItemDTO;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.entity.live.*;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.LiveCartDAO;
import com.project.pescueshop.util.constant.EnumCartStatus;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveCartService {
    private final LiveCartDAO cartDAO;
    private final LiveItemService liveItemService;
    private final LiveSessionService liveSessionService;

    public LiveCart createCartForNewUser(String userId, LiveSession liveSession) {
        LiveCart newCart = LiveCart.builder()
                .userId(userId)
                .sessionId(liveSession.getSessionId())
                .merchantId(liveSession.getMerchantId())
                .liveCartItemList(List.of())
                .status(EnumCartStatus.ACTIVE.getValue())
                .build();

        cartDAO.saveAndFlushLiveCart(newCart);
        return newCart;
    }

    public void createCartForNewUser(String userId, String sessionId) {
        LiveSession liveSession = liveSessionService.findBySessionId(sessionId);

        LiveCart cart = findCartByUserId(userId);
        if (cart == null) {
            cart = createCartForNewUser(userId, liveSession);
        }
        cart.setSessionId(sessionId);
        cartDAO.saveAndFlushLiveCart(cart);
    }

    public void createCartForNewUserAsync(String userId, String sessionKey) {
        if (userId == null) return;

        CompletableFuture.runAsync(() -> {
            if (findCartByUserId(userId) != null) {
                createCartForNewUser(userId, sessionKey);
            }
        });
    }

    public LiveCart findCartByUserId(String userId){
        return cartDAO.findByUserId(userId);
    }

    public LiveCart findCartByCartId(String cartId){
        return cartDAO.findByCartId(cartId);
    }

    public List<LiveCartItem> getLiveCartItemsByCartId(String cartId) {
        return getLiveCartItems(null, cartId);
    }

    private List<LiveCartItem> getLiveCartItems(String userId, String cartId) {
        LiveCart liveCart = cartDAO.findByLiveCartIdAndUserId(userId, cartId);

        return liveCart != null ? liveCart.getLiveCartItemList() : List.of();
    }

    public void addOrUpdateCartItem(AddOrUpdateLiveCartItemDTO dto, User user, LiveCart existedCart) throws FriendlyException {
        LiveItem liveItem = liveItemService.findByLiveItemId(dto.getLiveItemId());

        if (liveItem == null){
            throw new FriendlyException(EnumResponseCode.LIVE_ITEM_NOT_FOUND);
        }

        LiveCart cart = existedCart != null ? existedCart : findCartByUserId(user.getUserId());
        if (cart == null){
            throw new FriendlyException(EnumResponseCode.CART_NOT_FOUND);
        }

        LiveCartItem cartItem = cartDAO.findByLiveItemIdAndLiveCartId(dto.getLiveItemId(), cart.getLiveCartId());
        int newQuantity = cartItem == null ? dto.getQuantity() : cartItem.getQuantity() + dto.getQuantity();
        if (cartItem != null && newQuantity == 0){
            cart.getLiveCartItemList().remove(cartItem);
            cartDAO.saveAndFlushLiveCart(cart);
            return;
        } else if (newQuantity < 0) {
            return;
        }

        if (cartItem == null) {
            cartItem = new LiveCartItem();
        }
        cartItem.setLiveCartId(cart.getLiveCartId());
        cartItem.setLiveItem(liveItem);
        cartItem.setIsSelected(true);
        cartItem.setQuantity(newQuantity);
        cartItem.setTotalItemPrice(newQuantity * liveItem.getLivePrice());

        cartDAO.saveAndFlushLiveCartItem(cartItem);
    }

    public void selectCartItem(String cartItemId) throws FriendlyException {
        LiveCartItem cartItem = cartDAO.findLiveCartItemByLiveCartItemId(cartItemId);

        if (cartItem == null){
            throw new FriendlyException(EnumResponseCode.CART_ITEM_NOT_FOUND);
        }

        cartItem.setIsSelected(!cartItem.getIsSelected());

        cartDAO.saveAndFlushLiveCartItem(cartItem);
    }

    public void removeSelectedCartItem(String cartId) {
        cartDAO.removeSelectedCartItem(cartId);
    }
}
