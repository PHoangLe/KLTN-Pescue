package com.project.pescueshop.model.dto;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.entity.Voucher;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Name(noun = "invoice", pluralNoun = "invoiceList")
public class InvoiceListResultDTO {
    private String invoiceId;
    private String userId;
    private Date createdDate;
    private long totalPrice;
    private long discountPrice;
    private Long shippingFee;
    private long finalPrice;
    private Voucher voucher;
    private String streetName;
    private String wardName;
    private String districtName;
    private String cityName;
    private String phoneNumber;
    private String paymentType;
    private String status;
    private String userName;

    public InvoiceListResultDTO(Invoice invoice, String userName){
        this.invoiceId = invoice.getInvoiceId();
        this.userId = invoice.getUserId();
        this.createdDate = invoice.getCreatedDate();
        this.totalPrice = invoice.getTotalPrice();
        this.discountPrice = invoice.getDiscountPrice();
        this.shippingFee = invoice.getShippingFee();
        this.finalPrice = invoice.getFinalPrice();
        this.voucher = invoice.getVoucher();
        this.streetName = invoice.getStreetName();
        this.wardName = invoice.getWardName();
        this.districtName = invoice.getDistrictName();
        this.cityName = invoice.getCityName();
        this.phoneNumber = invoice.getPhoneNumber();
        this.paymentType = invoice.getPaymentType();
        this.status = invoice.getStatus();
        this.userName = userName;
    }
}
