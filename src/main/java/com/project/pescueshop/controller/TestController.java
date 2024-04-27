package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.ShippingFeeRequest;
import com.project.pescueshop.model.dto.ShippingFeeResponse;
import com.project.pescueshop.model.dto.ShippingItem;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.PaymentDAO;
import com.project.pescueshop.service.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@CrossOrigin
@Api
public class TestController {

    private final PaymentService paymentService;
    private final PaymentDAO paymentDAO;
    private final ShippingFeeService shippingFeeService;

    @GetMapping("")
    public Object findAllProduct() throws FriendlyException, UnsupportedEncodingException {
        ShippingFeeResponse response = shippingFeeService.calculateShippingFee(
                ShippingFeeRequest.builder()
                        .fromDistrictId(1454)
                        .toDistrictId(1454)
                        .fromWardCode("21211")
                        .toWardCode("21012")
                        .serviceTypeId(2)
                        .weight(1)
                        .items(
                                List.of(
                                        ShippingItem.builder()
                                                .height(10)
                                                .itemName("itemName")
                                                .length(10)
                                                .width(10)
                                                .quantity(10)
                                                .weight(10)
                                                .build(),
                                        ShippingItem.builder()
                                                .height(1)
                                                .itemName("itemName")
                                                .length(10)
                                                .width(10)
                                                .quantity(10)
                                                .weight(10)
                                                .build()
                                )
                        )
                        .build()
        );
        System.out.println(response.getData().getTotal());
        return ResponseEntity.ok("");
    }
}
