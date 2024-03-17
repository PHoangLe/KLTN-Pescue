package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.AddOrUpdateImportItemDTO;
import com.project.pescueshop.model.dto.ImportItemGroupDTO;
import com.project.pescueshop.model.dto.ImportItemListDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.ImportInvoice;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.ImportService;
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
@RequestMapping("/api/v1/import")
@CrossOrigin
@RequiredArgsConstructor
@Api
public class ImportController {
    private final ImportService importService;
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<ImportInvoice>> addNewImportInvoice(@RequestBody List<AddOrUpdateImportItemDTO> itemDTOList) throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        ImportInvoice importInvoice = importService.addNewImportInvoice(user, itemDTOList);

        ResponseDTO<ImportInvoice> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, importInvoice, "importInvoice");

        return ResponseEntity.ok(result);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<ImportInvoice>>> getAllImportInvoice(@RequestParam(required = false) Date fromDate, @RequestParam(required = false) Date toDate){
        List<ImportInvoice> itemList = importService.getAllImportInvoice(fromDate, toDate);
        ResponseDTO<List<ImportInvoice>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, itemList, "invoiceList");

        return ResponseEntity.ok(result);
    }

    @GetMapping("items")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<ImportItemListDTO>>> getImportItemList(
            @RequestParam String invoiceId,
            @RequestParam String productId){
        List<ImportItemListDTO> itemList = importService.getImportItemListByInvoiceId(invoiceId, productId);
        ResponseDTO<List<ImportItemListDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, itemList, "itemList");

        return ResponseEntity.ok(result);
    }

    @GetMapping("groups/{invoiceId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<ImportItemGroupDTO>>> getImportItemGroupsByInvoiceId(@PathVariable String invoiceId){
        List<ImportItemGroupDTO> itemList = importService.getImportItemGroupsByInvoiceId(invoiceId);
        ResponseDTO<List<ImportItemGroupDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, itemList, "itemGroup");

        return ResponseEntity.ok(result);
    }
}
