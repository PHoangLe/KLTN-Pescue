package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.*;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.PaymentService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin
@RequiredArgsConstructor
@Api
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/user-cart-checkout")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<CheckoutResultDTO>> cartCheckout(@RequestBody CartCheckOutInfoDTO cartCheckOutInfoDTO) throws FriendlyException, UnsupportedEncodingException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        CheckoutResultDTO paymentInfo = paymentService.userCartCheckoutAuthenticate(user, cartCheckOutInfoDTO);
        ResponseDTO<CheckoutResultDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, paymentInfo, "output");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/single-item-checkout")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<CheckoutResultDTO>> singleItemCheckout(@RequestBody SingleItemCheckOutInfoDTO singleItemCheckOutInfoDTO) throws FriendlyException, UnsupportedEncodingException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        CheckoutResultDTO paymentInfo = paymentService.singleItemCheckOutAuthenticate(user, singleItemCheckOutInfoDTO);
        ResponseDTO<CheckoutResultDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, paymentInfo, "output");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/un-authenticate/user-cart-checkout")
    public ResponseEntity<ResponseDTO<CheckoutResultDTO>> cartCheckoutUnAuthenticate(@RequestBody CartCheckOutInfoDTO cartCheckOutInfoDTO) throws FriendlyException {
        CheckoutResultDTO paymentInfo = paymentService.userCartCheckoutUnAuthenticate(cartCheckOutInfoDTO);
        ResponseDTO<CheckoutResultDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, paymentInfo, "output");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/un-authenticate/single-item-checkout")
    public ResponseEntity<ResponseDTO<CheckoutResultDTO>> singleItemCheckoutUnAuthenticate(@RequestBody SingleItemCheckOutInfoDTO singleItemCheckOutInfoDTO) throws FriendlyException, UnsupportedEncodingException {
        CheckoutResultDTO paymentInfo = paymentService.singleItemCheckOutUnAuthenticate(singleItemCheckOutInfoDTO);
        ResponseDTO<CheckoutResultDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, paymentInfo, "output");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/shipping-fee")
    public ResponseEntity<ResponseDTO<List<ShippingFeeDTO>>> singleItemCheckoutUnAuthenticate(@RequestBody GetCartShippingFeeRequest request) throws FriendlyException {
        List<ShippingFeeDTO> shippingFee = paymentService.getShippingFeeByCartId(request);
        ResponseDTO<List<ShippingFeeDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, shippingFee, "shippingFees");
        return ResponseEntity.ok(result);
    }
}