package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.*;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.entity.live.LiveInvoice;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.PaymentDAO;
import com.project.pescueshop.service.*;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@CrossOrigin
@Api
public class TestController {
    private final PaymentService paymentService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<ReportResultDTO>>> report() throws IOException {
        Invoice invoice = Invoice.builder().build();

        paymentService.pushDataToElastic(invoice, List.of(InvoiceItemDTO.builder()
                        .userId("1")
                        .invoiceId("1")
                        .productId("3")
                .build()));

        ResponseDTO<List<ReportResultDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, null);
        return ResponseEntity.ok(result);
    }
}
