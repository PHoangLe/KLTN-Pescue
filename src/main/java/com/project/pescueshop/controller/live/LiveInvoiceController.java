package com.project.pescueshop.controller.live;

import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.live.LiveInvoice;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.live.LiveInvoiceService;
import com.project.pescueshop.service.live.LivePaymentService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/live/invoice")
@CrossOrigin
@RequiredArgsConstructor
@Api
public class LiveInvoiceController {
    private static final int BASE_EPOCH_TIME = 0; // 1970-01-01 00:00:00
    private final LiveInvoiceService invoiceService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<Page<LiveInvoice>>> getAllInvoice(
            @RequestParam(required = false) Date fromDate,
            @RequestParam(required = false) Date toDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String paymentType) throws FriendlyException {
      fromDate = fromDate == null ? new Date(BASE_EPOCH_TIME) : fromDate;
      toDate = toDate == null ? new Date() : toDate;

      Page<LiveInvoice> invoice = invoiceService.getAllLiveInvoice(fromDate, toDate, page, size, status, paymentType);
      ResponseDTO<Page<LiveInvoice>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, invoice, "invoiceList");
      return ResponseEntity.ok(result);
    }
}
