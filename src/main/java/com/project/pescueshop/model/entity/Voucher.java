package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "VOUCHER")
@Entity
@AllArgsConstructor
@Builder
@Name(prefix = "VOCH")
public class Voucher {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String voucherId;
    private String type;
    private Date createdDate;
    private long value;
    private long price;
    private long maxValue;
    private long minInvoiceValue;
    private String status;
}
