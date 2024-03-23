package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.CreateMerchantRequest;
import com.project.pescueshop.model.dto.MerchantDTO;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.repository.dao.MerchantDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MerchantService extends BaseService {
    private final MerchantDAO merchantDAO;
    private final ThreadService threadService;

    public MerchantDTO toDTO(Merchant merchant){
        return MerchantDTO.builder()
                .merchantId(merchant.getMerchantId())
                .merchantName(merchant.getMerchantName())
                .merchantAvatar(merchant.getMerchantAvatar())
                .merchantCover(merchant.getMerchantCover())
                .merchantDescription(merchant.getMerchantDescription())
                .createdDate(merchant.getCreatedDate())
                .location(merchant.getLocation())
                .phoneNumber(merchant.getPhoneNumber())
                .userId(merchant.getUserId())
                .noProduct(merchant.getNoProduct())
                .relatedDocuments(merchant.getRelatedDocuments() == null ? new ArrayList<>() : merchant.getRelatedDocuments())
                .isSuspended(merchant.getIsSuspended())
                .isApproved(merchant.getIsApproved())
                .isLiveable(merchant.getIsLiveable())
                .build();
    }

    public MerchantDTO createNewMerchantRequest(User user, CreateMerchantRequest request, MultipartFile[] relatedDocumentsFile, MultipartFile avatarFile, MultipartFile coverImageFile) {
        Merchant merchant = Merchant.builder()
                .merchantName(request.getMerchantName())
                .merchantDescription(request.getMerchantDescription())
                .createdDate(new Date())
                .location(request.getLocation())
                .phoneNumber(request.getPhoneNumber())
                .userId(user.getUserId())
                .noProduct(0)
                .isSuspended(false)
                .isApproved(false)
                .isLiveable(true)
                .build();

        merchantDAO.saveAndFlushMerchant(merchant);

        threadService.uploadMerchantFiles(merchant, List.of(relatedDocumentsFile), avatarFile, coverImageFile);

        merchantDAO.saveAndFlushMerchant(merchant);

        return toDTO(merchant);
    }
}
