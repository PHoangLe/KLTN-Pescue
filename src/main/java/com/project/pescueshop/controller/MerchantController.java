package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.CreateMerchantRequest;
import com.project.pescueshop.model.dto.InvoiceItemDTO;
import com.project.pescueshop.model.dto.MerchantDTO;
import com.project.pescueshop.model.dto.ProductDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.MerchantService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchant")
@CrossOrigin
@RequiredArgsConstructor
@Api
public class MerchantController {
    private final MerchantService merchantService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<MerchantDTO>> createNewMerchant(@RequestPart CreateMerchantRequest createMerchantRequest,
                                                                      @RequestPart("relatedDocuments") MultipartFile[] relatedDocumentsFile,
                                                                      @RequestPart("avatar") MultipartFile avatarFile,
                                                                      @RequestPart("coverImage") MultipartFile coverImageFile) throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();

        MerchantDTO merchantDTO = merchantService.createNewMerchantRequest(user, createMerchantRequest, relatedDocumentsFile, avatarFile, coverImageFile);

        ResponseDTO<MerchantDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, merchantDTO, "merchant");
        return ResponseEntity.ok(result);
    }
}
