package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.CartDTO;
import com.project.pescueshop.model.dto.CartItemDTO;
import com.project.pescueshop.model.dto.AddOrUpdateCartItemDTO;
import com.project.pescueshop.model.dto.MerchantGroupCartItem;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.CartItem;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.CartService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin
@RequiredArgsConstructor
@Api
public class CartController {
    private final CartService cartService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<MerchantGroupCartItem>>> getCart() throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        List<MerchantGroupCartItem> itemList = cartService.getCartItemByUserId(user.getUserId());
        ResponseDTO<List<MerchantGroupCartItem>> result;
        if (itemList == null) {
             result = new ResponseDTO<>(EnumResponseCode.CART_NOT_FOUND);
        }
        else {
            result = new ResponseDTO<>(EnumResponseCode.SUCCESS, itemList, "cartItems");
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/un-authenticate")
    public ResponseEntity<ResponseDTO<CartDTO>> getCartUnAuthenticate(@RequestParam(required = false) String cartId) throws FriendlyException {
        CartDTO itemList = cartService.getUnAuthenticatedCart(cartId);

        EnumResponseCode responseCode = (itemList == null) ? EnumResponseCode.CART_NOT_FOUND : EnumResponseCode.SUCCESS;

        ResponseDTO<CartDTO> result = new ResponseDTO<>(responseCode, itemList, "cart");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/un-authenticate/update-cart-item/{cartId}")
    public ResponseEntity<ResponseDTO<CartItem>> addItemToCartUnAuthenticate(@RequestBody AddOrUpdateCartItemDTO dto, @PathVariable String cartId) throws FriendlyException {
        cartService.addOrUpdateUnAuthenticatedCartItem(dto, cartId);

        ResponseDTO<CartItem> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update-cart-item")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<CartItem>> addItemToCart(@RequestBody AddOrUpdateCartItemDTO dto) throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        cartService.addOrUpdateCartItem(dto, user, null);

        ResponseDTO<CartItem> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/select-cart-item/{cartItemId}")
    public ResponseEntity<ResponseDTO<CartItem>> selectCartItem(@PathVariable String cartItemId) throws FriendlyException {
        cartService.selectCartItem(cartItemId);

        ResponseDTO<CartItem> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);
        return ResponseEntity.ok(result);
    }
}
