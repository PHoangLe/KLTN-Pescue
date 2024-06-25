package com.project.pescueshop.model.entity.live;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.entity.Voucher;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "LIVE_INVOICE")
@Entity
@Name(prefix = "LIIV", noun = "liveInvoice")
public class LiveInvoice {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String liveInvoiceId;
    private String liveSessionId;
    private String merchantId;
    private String userId;
    private Date createdDate;
    private Integer amount;
    private long initialPrice;
    private long livePrice;
    private Long shippingFee;
    private long discountPrice;
    private long totalPrice;
    private long finalPrice;
    @OneToOne
    @JoinColumn(name = "voucherId", referencedColumnName = "voucherId")
    private Voucher voucher;
    // shipping info
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
