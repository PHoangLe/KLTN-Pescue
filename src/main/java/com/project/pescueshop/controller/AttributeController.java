package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.CreateVarietyAttributeRequest;
import com.project.pescueshop.model.dto.ProductDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.VarietyAttribute;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AttributeService;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attributes")
@RequiredArgsConstructor
@CrossOrigin
@Api
public class AttributeController {
    private final AttributeService attributeService;
    private final AuthenticationService authenticationService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<VarietyAttribute>> addAttribute(@RequestBody CreateVarietyAttributeRequest request) throws FriendlyException, InterruptedException {
        Merchant currentMerchant = authenticationService.getCurrentMerchant();

        VarietyAttribute varietyAttribute = attributeService.addAttribute(request, currentMerchant);
        ResponseDTO<VarietyAttribute> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, varietyAttribute, "varietyAttribute");

        return ResponseEntity.ok(result);
    }
}
