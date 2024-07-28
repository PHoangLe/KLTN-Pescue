package com.project.pescueshop.controller.live;

import com.project.pescueshop.model.dto.*;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.entity.live.LiveInvoice;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.live.LiveInvoiceService;
import com.project.pescueshop.service.live.LivePaymentService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/live/payment")
@Slf4j
public class LivePaymentController {
    private final LivePaymentService paymentService;

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
    public ResponseEntity<ResponseDTO<CheckoutResultDTO>> singleItemCheckout(@RequestBody SingleLiveItemCheckOutInfoDTO singleItemCheckOutInfoDTO) throws FriendlyException, UnsupportedEncodingException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        CheckoutResultDTO paymentInfo = paymentService.singleItemCheckOutAuthenticate(user, singleItemCheckOutInfoDTO);
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
