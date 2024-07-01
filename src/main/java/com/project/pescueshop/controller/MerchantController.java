package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.*;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.MerchantService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    public ResponseEntity<ResponseDTO<MerchantDTO>> merchantRegistry(@RequestPart("createMerchantRequest") CreateMerchantRequest createMerchantRequest,
                                                                      @RequestPart("relatedDocuments") MultipartFile[] relatedDocumentsFile,
                                                                      @RequestPart("avatar") MultipartFile avatarFile,
                                                                      @RequestPart("coverImage") MultipartFile coverImageFile) throws FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUser();

        MerchantDTO merchantDTO = merchantService.createNewMerchantRequest(user, createMerchantRequest, relatedDocumentsFile, avatarFile, coverImageFile);

        ResponseDTO<MerchantDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, merchantDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{merchantId}")
    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<MerchantDTO>> getMerchantInfo(@PathVariable(required = false) String merchantId) throws FriendlyException {
        MerchantDTO merchantDTO = merchantService.getMerchantInfo(merchantId);
        ResponseDTO<MerchantDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, merchantDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/customer/{merchantId}")
    public ResponseEntity<ResponseDTO<MerchantDTO>> getMerchantPageInfo(@PathVariable String merchantId) throws FriendlyException {
        MerchantDTO merchantDTO = merchantService.getMerchantPageInfo(merchantId);
        ResponseDTO<MerchantDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, merchantDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/info")
    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<String>> updateMerchantInfo(
            @RequestPart("updateMerchantInfoRequest") UpdateMerchantInfoRequest updateMerchantInfoRequest,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImageFile) throws FriendlyException, ExecutionException, InterruptedException {
        MerchantDTO dto = merchantService.updateMerchantInfo(updateMerchantInfoRequest, avatarFile, coverImageFile);
        ResponseDTO<String> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, "Success", "message");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/suspend/{merchantId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<String>> suspendMerchant(@PathVariable String merchantId) throws FriendlyException {
        merchantService.suspendMerchant(merchantId);
        ResponseDTO<String> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, "Success", "message");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/un-suspend/{merchantId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<String>> unsuspendMerchant(@PathVariable String merchantId) throws FriendlyException {
        merchantService.unsuspendMerchant(merchantId);
        ResponseDTO<String> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, "Success", "message");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/approve/{merchantId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<String>> approveMerchant(@PathVariable String merchantId) throws FriendlyException {
        merchantService.approveMerchant(merchantId);
        ResponseDTO<String> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, "Success", "message");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/un-approve/{merchantId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<String>> unapproveMerchant(@PathVariable String merchantId) throws FriendlyException {
        merchantService.unapproveMerchant(merchantId);
        ResponseDTO<String> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, "Success", "message");
        return ResponseEntity.ok(result);
    }

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<MerchantDTO>>> getAllMerchant() {
        List<MerchantDTO> merchants = merchantService.getApproveMerchant();
        ResponseDTO<List<MerchantDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, merchants, "merchantList");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<Page<MerchantDTO>>> getAllMerchantAdmin(@RequestParam(required = false) Boolean isApproved,
                                                                              @RequestParam(required = false) Boolean isSuspended,
                                                                              @RequestParam(required = false) Boolean isLiveable,
                                                                              @RequestParam(defaultValue = "1") int offset, @RequestParam(defaultValue = "10") int limit) {
        Page<MerchantDTO> merchants = merchantService.getListMerchantForAdmin(isApproved, isSuspended, isLiveable, offset, limit);
        ResponseDTO<Page<MerchantDTO>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, merchants, "merchantList");
        return ResponseEntity.ok(result);
    }
}
