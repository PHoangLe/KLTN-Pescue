package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.CreateVarietyAttributeRequest;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.VarietyAttribute;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.VarietyAttributeDAO;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttributeService {
    VarietyAttributeDAO varietyAttributeDAO;

    public VarietyAttribute addAttribute(CreateVarietyAttributeRequest request, Merchant merchant) throws FriendlyException {
        VarietyAttribute attribute = VarietyAttribute.builder()
                .value(request.getValue())
                .name(request.getName())
                .type(request.getType())
                .merchantId(merchant.getMerchantId())
                .build();
        try {
            varietyAttributeDAO.saveAndFlushAttribute(attribute);
        } catch (Exception e) {
            log.trace("Error while saving attribute", e);
            throw new FriendlyException(EnumResponseCode.MERCHANT_NOT_FOUND);
        }
        return attribute;
    }
}
