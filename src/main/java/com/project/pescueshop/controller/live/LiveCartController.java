package com.project.pescueshop.controller.live;

import com.project.pescueshop.model.dto.AddOrUpdateLiveCartItemDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.CartItem;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.entity.live.LiveCart;
import com.project.pescueshop.model.entity.live.LiveCartItem;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.live.LiveCartService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/live/cart")
@Slf4j
public class LiveCartController {
    private final LiveCartService cartService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<LiveCartItem>>> getCart() throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        List<LiveCartItem> itemList = cartService.getLiveCartItemsByCartId(user.getUserId());
        ResponseDTO<List<LiveCartItem>> result;
        if (itemList == null) {
            result = new ResponseDTO<>(EnumResponseCode.CART_NOT_FOUND);
        }
        else {
            result = new ResponseDTO<>(EnumResponseCode.SUCCESS, itemList, "cartItems");
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/un-authenticate")
    public ResponseEntity<ResponseDTO<LiveCart>> getCartUnAuthenticate(@RequestParam String cartId, @RequestParam String sessionId) {
        LiveCart itemList = cartService.getUnAuthenticatedCart(cartId, sessionId);

        EnumResponseCode responseCode = (itemList == null) ? EnumResponseCode.CART_NOT_FOUND : EnumResponseCode.SUCCESS;

        ResponseDTO<LiveCart> result = new ResponseDTO<>(responseCode, itemList, "cart");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/un-authenticate/update-cart-item/{cartId}")
    public ResponseEntity<ResponseDTO<LiveCartItem>> addItemToCartUnAuthenticate(@RequestBody AddOrUpdateLiveCartItemDTO dto, @PathVariable String cartId) throws FriendlyException {
        cartService.addOrUpdateUnAuthenticatedCartItem(dto, cartId);

        ResponseDTO<LiveCartItem> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update-cart-item")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<LiveCartItem>> addItemToCart(@RequestBody AddOrUpdateLiveCartItemDTO dto) throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        cartService.addOrUpdateCartItem(dto, user, null);

        ResponseDTO<LiveCartItem> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/select-cart-item/{cartItemId}")
    public ResponseEntity<ResponseDTO<CartItem>> selectCartItem(@PathVariable String cartItemId) throws FriendlyException {
        cartService.selectCartItem(cartItemId);

        ResponseDTO<CartItem> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);
        return ResponseEntity.ok(result);
    }
}
