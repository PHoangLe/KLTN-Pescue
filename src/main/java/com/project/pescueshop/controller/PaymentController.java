package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.CreatePaymentLinkDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.PaymentService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin
@RequiredArgsConstructor
@Api
public class PaymentController {
    private final PaymentService paymentService;
    
    @PutMapping("/createPaymentLink")
    public ResponseEntity<ResponseDTO<String>> singleItemCheckoutUnAuthenticate(@RequestBody CreatePaymentLinkDTO dto) throws FriendlyException, UnsupportedEncodingException {
        String paymentLink = paymentService.createPaymentLink(dto.getContent(), dto.getReturnUrl(), dto.getValue());
        ResponseDTO<String> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, paymentLink, "paymentLink");
        return ResponseEntity.ok(result);
    }
}
