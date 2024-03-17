package com.project.pescueshop.controller;


import com.project.pescueshop.model.dto.InvoiceItemDTO;
import com.project.pescueshop.model.dto.VoucherInputDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.entity.Voucher;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.InvoiceService;
import com.project.pescueshop.service.VoucherService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/voucher")
@CrossOrigin
@RequiredArgsConstructor
@Api
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<Voucher>>> getAllInvoice(){
        List<Voucher> voucherList = voucherService.findAllVoucher();
        ResponseDTO<List<Voucher>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, voucherList, "voucherList");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/available-voucher")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<Voucher>>> getAllAvailableVoucher() throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        List<Voucher> voucherList = voucherService.findAllAvailabeVoucher(user);
        ResponseDTO<List<Voucher>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, voucherList, "voucherList");
        return ResponseEntity.ok(result);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<Voucher>> createNewVoucher(@RequestBody VoucherInputDTO dto) {
        Voucher voucher = voucherService.createNewVoucher(dto);
        ResponseDTO<Voucher> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, voucher, "voucher");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{voucherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<Voucher>> updateVoucher(@RequestBody VoucherInputDTO dto, @PathVariable String voucherId) throws FriendlyException {
        Voucher voucher = voucherService.updateVoucher(voucherId, dto);
        ResponseDTO<Voucher> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, voucher, "voucher");
        return ResponseEntity.ok(result);
    }

}
