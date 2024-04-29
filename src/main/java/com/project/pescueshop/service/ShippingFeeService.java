package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.InvoiceItemDTO;
import com.project.pescueshop.model.dto.ShippingFeeRequest;
import com.project.pescueshop.model.dto.ShippingFeeResponse;
import com.project.pescueshop.model.dto.ShippingItem;
import com.project.pescueshop.model.entity.Address;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.Variety;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingFeeService {

    private static final String TOKEN = "12a3810e-8ba7-11ee-a59f-a260851ba65c";
    private static final Integer SHOP_ID = 4723073;

    public ShippingFeeResponse calculateShippingFee(ShippingFeeRequest request) throws FriendlyException {
        String url = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
        RestTemplate restTemplate = new RestTemplate();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", TOKEN);
        headers.set("ShopId", SHOP_ID.toString());

        request.setServiceTypeId(2);

        HttpEntity entity = new HttpEntity(request, headers);

        try{
            ResponseEntity<ShippingFeeResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, ShippingFeeResponse.class);

            return response.getBody();
        }
        catch(Exception e){
            log.error("Error when calling GHN API", e);
            throw new FriendlyException(EnumResponseCode.SHIPPING_FEE_ERROR);
        }
    }

    public long calculateShippingFee(List<InvoiceItemDTO> invoiceItemDTO, Address toAddress, Merchant merchant) throws FriendlyException {
        List<ShippingItem> items = invoiceItemDTO.stream()
                .map(item -> ShippingItem.builder()
                        .itemId(item.getVarietyId())
                        .itemName(item.getName())
                        .height(item.getHeight())
                        .length(item.getLength())
                        .weight(item.getWeight())
                        .width(item.getWidth())
                        .quantity(item.getQuantity())
                        .build())
                .toList();

        ShippingFeeResponse response = calculateShippingFee(ShippingFeeRequest.builder()
                .toDistrictId(toAddress.getDistrictId())
                .toWardCode(toAddress.getWardCode())
                .fromDistrictId(merchant.getDistrictId())
                .fromWardCode(merchant.getWardCode())
                .weight(invoiceItemDTO.stream().mapToInt(InvoiceItemDTO::getWeight).sum())
                .items(items)
                .build());

        return response.getData().getTotal();
    }

    public long calculateShippingFee(Variety variety, Address toAddress, Merchant merchant) throws FriendlyException {
        List<ShippingItem> items = new ArrayList<>();
        items.add(ShippingItem.builder()
                .itemId(variety.getVarietyId())
                .itemName(variety.getName())
                .height(variety.getHeight())
                .length(variety.getLength())
                .weight(variety.getWeight())
                .width(variety.getWidth())
                .quantity(1)
                .build());

        ShippingFeeResponse response = calculateShippingFee(ShippingFeeRequest.builder()
                .toDistrictId(toAddress.getDistrictId())
                .toWardCode(toAddress.getWardCode())
                .fromDistrictId(merchant.getDistrictId())
                .fromWardCode(merchant.getWardCode())
                .weight(variety.getWeight())
                .items(items)
                .build());

        return response.getData().getTotal();
    }
}