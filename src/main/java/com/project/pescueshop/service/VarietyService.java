package com.project.pescueshop.service;


import com.project.pescueshop.model.dto.UpdateVarietyMeasurementRequest;
import com.project.pescueshop.model.dto.UpdateVarietyStockAmountRequest;
import com.project.pescueshop.model.dto.VarietyDTO;
import com.project.pescueshop.model.entity.Product;
import com.project.pescueshop.model.entity.Variety;
import com.project.pescueshop.model.entity.VarietyAttribute;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.jpa.VarietyAttributeRepository;
import com.project.pescueshop.repository.jpa.VarietyRepository;
import com.project.pescueshop.util.constant.EnumResponseCode;
import com.project.pescueshop.util.constant.EnumStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VarietyService extends BaseService{
    private final VarietyRepository varietyRepository;
    private final VarietyAttributeRepository varietyAttributeRepository;
    private final ThreadService threadService;

    public VarietyDTO transformVarietyToDTO(Variety variety){
        return new VarietyDTO(variety);
    }

    public List<VarietyDTO> transformVarietyToDTOList(List<Variety> varietyList){
        if (CollectionUtils.isEmpty(varietyList)){
            return new ArrayList<>();
        }

        return varietyList.stream()
                .map(this::transformVarietyToDTO).toList();
    }

    public Variety findById(String id){
        return varietyRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackOn = Exception.class)
    public VarietyDTO addOrUpdateVariety(VarietyDTO dto){
        Variety result = addOrUpdateVariety(new Variety(dto));

        result = addOrUpdateVariety(result);

        return transformVarietyToDTO(result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Variety addOrUpdateVariety(Variety variety){
        varietyRepository.saveAndFlush(variety);
        return variety;
    }

    public Map<String, List<VarietyAttribute>> findAllVarietyAttribute(){
        List<VarietyAttribute> varietyAttributeList = varietyAttributeRepository.findAll();

        return varietyAttributeList.stream()
                .collect(Collectors.groupingBy(VarietyAttribute::getType));
    }

    public void addVarietyByListAttribute(Product product, List<VarietyAttribute> varietyAttributeList, boolean isSameMeasurement){
        for (VarietyAttribute attribute: varietyAttributeList){
            Variety variety = new Variety();
            variety.addAttribute(attribute);
            variety.setMerchantId(product.getMerchantId());
            variety.setProductId(product.getProductId());
            variety.setName(product.getName());
            variety.setStatus(EnumStatus.ACTIVE.getValue());
            variety.setPrice(product.getPrice());

            if (isSameMeasurement) {
                variety.setWidth(product.getWidth());
                variety.setHeight(product.getHeight());
                variety.setLength(product.getLength());
                variety.setWeight(product.getWeight());
            }

            addOrUpdateVariety(variety);
        }
    }

    public void addVarietyByListAttribute(Product product, List<VarietyAttribute> sizeAttributesList, List<VarietyAttribute> colorAttributeList, boolean isSameMeasurement) throws InterruptedException {
        List<VarietyAttribute> attributesList = CollectionUtils.isEmpty(sizeAttributesList) ? colorAttributeList : sizeAttributesList;
        if (!CollectionUtils.isEmpty(attributesList)) {
            if (!CollectionUtils.isEmpty(colorAttributeList) && !CollectionUtils.isEmpty(sizeAttributesList)) {
                threadService.addVarietyByAttribute(product, sizeAttributesList, colorAttributeList, isSameMeasurement);
            } else {
                addVarietyByListAttribute(product, attributesList, isSameMeasurement);
            }
        }
    }

    public List<Variety> findByProductId(String productId){
        return varietyRepository.findByProductId(productId);
    }

    @Transactional(rollbackOn = Exception.class)
    public void updateVarietyMeasurement(List<UpdateVarietyMeasurementRequest> request) throws FriendlyException {
        for (UpdateVarietyMeasurementRequest updateRequest : request) {
            Variety variety = findById(updateRequest.getVarietyId());
            if (variety == null) {
                throw new FriendlyException(EnumResponseCode.VARIETY_NOT_FOUND);
            }
                variety.setWidth(updateRequest.getWidth());
                variety.setHeight(updateRequest.getHeight());
                variety.setLength(updateRequest.getLength());
                variety.setWeight(updateRequest.getWeight());

                addOrUpdateVariety(variety);
        }
    }

    public String getCoverImageById(String varietyId) {
        return varietyRepository.getCoverImageById(varietyId);
    }

    public void updateVarietyStockAmount(UpdateVarietyStockAmountRequest request) throws FriendlyException {
        if (request.getVarietyId() == null){
            throw new FriendlyException(EnumResponseCode.INVALID_PARAMS);
        }

        if (request.getStockAmount() < 0) {
            request.setStockAmount(0);
        }

        Variety variety = findById(request.getVarietyId());
        if (variety == null){
            throw new FriendlyException(EnumResponseCode.VARIETY_NOT_FOUND);
        }

        variety.setStockAmount(request.getStockAmount());

        addOrUpdateVariety(variety);
    }
}
