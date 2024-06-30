package com.project.pescueshop.service;

import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.project.pescueshop.model.dto.CreateMerchantRequest;
import com.project.pescueshop.model.dto.MerchantDTO;
import com.project.pescueshop.model.dto.UpdateMerchantInfoRequest;
import com.project.pescueshop.model.elastic.ElasticClient;
import com.project.pescueshop.model.elastic.document.MerchantData;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.MerchantDAO;
import com.project.pescueshop.util.constant.EnumElasticIndex;
import com.project.pescueshop.util.constant.EnumResponseCode;
import com.project.pescueshop.util.constant.EnumRoleId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.concurrent.CompletedFuture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Slf4j
public class MerchantService extends BaseService {
    private final MerchantDAO merchantDAO;
    private final ThreadService threadService;
    private final UserService userService;

    public MerchantDTO toDTO(Merchant merchant){
        return MerchantDTO.builder()
                .merchantId(merchant.getMerchantId())
                .merchantName(merchant.getMerchantName())
                .merchantAvatar(merchant.getMerchantAvatar())
                .merchantCover(merchant.getMerchantCover())
                .merchantDescription(merchant.getMerchantDescription())
                .createdDate(merchant.getCreatedDate())
                .cityName(merchant.getCityName())
                .districtName(merchant.getDistrictName())
                .wardName(merchant.getWardName())
                .cityCode(merchant.getCityCode())
                .districtId(merchant.getDistrictId())
                .wardCode(merchant.getWardCode())
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
                .cityName(request.getCityName())
                .districtName(request.getDistrictName())
                .wardName(request.getWardName())
                .cityCode(request.getCityCode())
                .districtId(request.getDistrictId())
                .wardCode(request.getWardCode())
                .phoneNumber(request.getPhoneNumber())
                .userId(user.getUserId())
                .noProduct(0)
                .isSuspended(false)
                .isLiveable(true)
                .isApproved(false)
                .build();

        merchantDAO.saveAndFlushMerchant(merchant);
        threadService.uploadMerchantFiles(merchant, List.of(relatedDocumentsFile), avatarFile, coverImageFile);
        merchantDAO.saveAndFlushMerchant(merchant);

        CompletableFuture.runAsync(() -> {
            pushMerchantToElasticsearch(merchant);
        });

        return toDTO(merchant);
    }

    public void suspendMerchant(String merchantId) throws FriendlyException {
        Merchant merchant = merchantDAO.getMerchantById(merchantId);

        if (merchant == null) {
            throw new FriendlyException(EnumResponseCode.MERCHANT_NOT_FOUND);
        }

        merchant.setIsSuspended(true);
        merchantDAO.saveAndFlushMerchant(merchant);

        CompletableFuture.runAsync(() -> {
            updateMerchantInElasticsearch(merchant);
        });
    }

    public void unsuspendMerchant(String merchantId) throws FriendlyException {
        Merchant merchant = merchantDAO.getMerchantById(merchantId);

        if (merchant == null) {
            throw new FriendlyException(EnumResponseCode.MERCHANT_NOT_FOUND);
        }

        merchant.setIsSuspended(false);
        merchantDAO.saveAndFlushMerchant(merchant);


        CompletableFuture.runAsync(() -> {
            updateMerchantInElasticsearch(merchant);
        });
    }

    public void approveMerchant(String merchantId) throws FriendlyException {
        Merchant merchant = merchantDAO.getMerchantById(merchantId);

        if (merchant == null) {
            throw new FriendlyException(EnumResponseCode.MERCHANT_NOT_FOUND);
        }

        CompletableFuture.runAsync(() -> {
            try {
                userService.addUserRole(merchant.getUserId(), EnumRoleId.MERCHANT);
            } catch (FriendlyException e) {
                throw new RuntimeException(e);
            }
        });

        merchant.setIsApproved(true);
        merchantDAO.saveAndFlushMerchant(merchant);

        CompletableFuture.runAsync(() -> {
            updateMerchantInElasticsearch(merchant);
        });
    }

    public void unapproveMerchant(String merchantId) throws FriendlyException {
        Merchant merchant = merchantDAO.getMerchantById(merchantId);

        if (merchant == null) {
            throw new FriendlyException(EnumResponseCode.MERCHANT_NOT_FOUND);
        }

        merchantDAO.deleteMerchant(merchantId);

        CompletableFuture.runAsync(() -> {
            deleteMerchantFromElasticsearch(merchantId);
        });
    }

    public Merchant getMerchantById(String merchantId) {
        return merchantDAO.getMerchantById(merchantId);
    }

    public Merchant getMerchantByUserId(String userId) {
        return merchantDAO.getMerchantByUserId(userId);
    }

    public MerchantDTO getMerchantInfo(String merchantId) throws FriendlyException {
        Merchant merchant;

        if (merchantId != null) {
            merchant = getMerchantById(merchantId);
        }
        else {
            User user = AuthenticationService.getCurrentLoggedInUser();
            merchant = merchantDAO.getMerchantByUserId(user.getUserId());
        }

        if (merchant == null) {
            throw new FriendlyException(EnumResponseCode.MERCHANT_NOT_FOUND);
        }

        if (merchant.getIsSuspended()){
            throw new FriendlyException(EnumResponseCode.MERCHANT_SUSPENDED);
        }

        return toDTO(merchant);
    }

    public MerchantDTO getMerchantPageInfo(String merchantId) throws FriendlyException {
        MerchantDTO dto = getMerchantInfo(merchantId);
        dto.setIsApproved(null);
        dto.setIsLiveable(null);
        dto.setIsSuspended(null);
        dto.setRelatedDocuments(null);

        return dto;
    }

    public List<MerchantDTO> getApproveMerchant() {
        return merchantDAO.getApprovedMerchant().stream()
                .map(this::toDTO)
                .toList();
    }

    public Page<MerchantDTO> getListMerchantForAdmin(Boolean isApproved, Boolean isSuspended, Boolean isLiveable, Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset - 1, limit);
        return merchantDAO.getListMerchantForAdmin(isApproved, isSuspended, isLiveable, pageable).map(this::toDTO);
    }

    public MerchantDTO updateMerchantInfo(
            UpdateMerchantInfoRequest updateMerchantInfoRequest,
            MultipartFile avatarFile,
            MultipartFile coverImageFile) throws FriendlyException {
        Merchant merchant = getMerchantById(updateMerchantInfoRequest.getMerchantId());
        if (merchant == null) {
            throw new FriendlyException(EnumResponseCode.MERCHANT_NOT_FOUND);
        }

        merchant.setMerchantName(updateMerchantInfoRequest.getMerchantName());
        merchant.setMerchantDescription(updateMerchantInfoRequest.getMerchantDescription());
        merchant.setCityName(updateMerchantInfoRequest.getCityName());
        merchant.setDistrictName(updateMerchantInfoRequest.getDistrictName());
        merchant.setWardName(updateMerchantInfoRequest.getWardName());
        merchant.setCityCode(updateMerchantInfoRequest.getCityCode());
        merchant.setDistrictId(updateMerchantInfoRequest.getDistrictId());
        merchant.setWardCode(updateMerchantInfoRequest.getWardCode());
        merchant.setPhoneNumber(updateMerchantInfoRequest.getPhoneNumber());

        CompletableFuture.runAsync(() -> {
            if (avatarFile != null) {
                threadService.uploadMerchantAvatar(merchant.getMerchantId(), avatarFile);
            }

            if (coverImageFile != null) {
                threadService.uploadMerchantCover(merchant.getMerchantId(), coverImageFile);
            }
        });

        merchantDAO.saveAndFlushMerchant(merchant);
        return toDTO(merchant);
    }

    private void pushMerchantToElasticsearch(Merchant merchant) {
        MerchantData merchantData = MerchantData.fromMerchant(merchant);

        IndexRequest<MerchantData> req = IndexRequest.of(i -> i
                .index(EnumElasticIndex.MERCHANT_DATA.getName())
                .id(merchantData.getMerchantId())
                .document(merchantData)
        );

        try {
            IndexResponse resp = ElasticClient.get().index(req);
            log.info("Pushed merchant to elasticsearch: {} document: {}", resp, merchantData);
        }
        catch (IOException e) {
            log.error("Error pushing merchant to elastic document: {} error: ", merchantData, e);
        }
    }

    private void updateMerchantInElasticsearch(Merchant merchant) {
        MerchantData merchantData = MerchantData.fromMerchant(merchant);

        IndexRequest<MerchantData> req = IndexRequest.of(i -> i
                .index(EnumElasticIndex.MERCHANT_DATA.getName())
                .id(merchantData.getMerchantId())
                .document(merchantData)
        );

        try {
            IndexResponse resp = ElasticClient.get().index(req);
            log.info("Updated merchant in elasticsearch: {} document: {}", resp, merchantData);
        }
        catch (IOException e) {
            log.error("Error updating merchant in elastic document: {} error: ", merchantData, e);
        }
    }

    private void deleteMerchantFromElasticsearch(String merchantId) {
        DeleteRequest req = DeleteRequest.of(i -> i
                .index(EnumElasticIndex.MERCHANT_DATA.getName())
                .id(merchantId)
        );

        try {
            DeleteResponse resp = ElasticClient.get().delete(req);
            log.info("Deleted merchant from elasticsearch: {} id: {}", resp, merchantId);
        }
        catch (IOException e) {
            log.error("Error deleting merchant from elastic id: {} error: ", merchantId, e);
        }
    }
}
