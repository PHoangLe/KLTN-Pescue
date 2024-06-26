package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "INVOICE")
@Entity
@Name(prefix = "INVO")
public class Invoice {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String invoiceId;
    private String merchantId;
    private String userId;
    private Date createdDate;
    private long totalPrice;
    private long discountPrice;
    private Long shippingFee;
    private long finalPrice;
    @OneToOne
    @JoinColumn(name = "voucherId", referencedColumnName = "voucherId")
    private Voucher voucher;
    private String streetName;
    private String wardName;
    private String wardCode;
    private String districtName;
    private Integer districtId;
    private String cityName;
    private String phoneNumber;
    private String userEmail;
    private String paymentType;
    private String status;
    private String recipientName;
}
