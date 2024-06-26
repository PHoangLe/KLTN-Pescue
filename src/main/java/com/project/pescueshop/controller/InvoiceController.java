package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.InvoiceItemDTO;
import com.project.pescueshop.model.dto.InvoiceListResultDTO;
import com.project.pescueshop.model.dto.InvoicesListDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.InvoiceService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/invoice")
@CrossOrigin
@RequiredArgsConstructor
@Api
public class InvoiceController {
    private static final int BASE_EPOCH_TIME = 0; // 1970-01-01 00:00:00
    private final InvoiceService invoiceService;

    @GetMapping("/{invoiceId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<InvoiceItemDTO>>> getInvoiceDetail(@PathVariable String invoiceId){
        List<InvoiceItemDTO> invoiceItemDTOS = invoiceService.getInvoiceDetail(invoiceId);
        ResponseDTO<List<InvoiceItemDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, invoiceItemDTOS, "invoiceItemList");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/live/{liveInvoiceId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<InvoiceItemDTO>>> getLiveInvoiceDetail(@PathVariable String liveInvoiceId){
        List<InvoiceItemDTO> invoiceItemDTOS = invoiceService.getLiveInvoiceDetail(liveInvoiceId);
        ResponseDTO<List<InvoiceItemDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, invoiceItemDTOS, "invoiceItemList");
        return ResponseEntity.ok(result);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<InvoiceListResultDTO>>> getAllInvoice(@RequestParam(required = false) Date fromDate, @RequestParam(required = false) Date toDate) throws FriendlyException {
        fromDate = fromDate == null ? new Date(BASE_EPOCH_TIME) : fromDate;
        toDate = toDate == null ? new Date() : toDate;

        List<InvoiceListResultDTO> invoice = invoiceService.findAllInvoice(fromDate, toDate);
        ResponseDTO<List<InvoiceListResultDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, invoice, "invoiceList");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-status")
    public ResponseEntity<ResponseDTO<Invoice>> getAllInvoice(
            @RequestParam String invoiceId,
            @RequestParam String status
    ) throws FriendlyException {
        Invoice invoice = invoiceService.updateInvoiceStatus(invoiceId, status);
        ResponseDTO<Invoice> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, invoice, "invoice");
        return ResponseEntity.ok(result);
    }


    @GetMapping("/user-invoice-info")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<InvoicesListDTO>> getInvoiceInfo() throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        InvoicesListDTO invoiceList = invoiceService.getAllInvoiceInfoByUser(user);
        ResponseDTO<InvoicesListDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, invoiceList, "invoiceList");
        return ResponseEntity.ok(result);
    }

}
