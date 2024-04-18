package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "MERCHANT")
@Entity
@Builder
@AllArgsConstructor
@Name(prefix = "MERC")
public class Merchant {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String merchantId;
    private String userId;
    private String merchantName;
    private String merchantDescription;
    private Date createdDate;
    private String merchantAvatar;
    private String merchantCover;
    private Integer rating;
    private String cityName;
    private String districtName;
    private String wardName;
    private String cityCode;
    private String districtCode;
    private String wardCode;
    private String phoneNumber;
    private Integer noProduct;
    @ElementCollection
    private List<String> relatedDocuments;
    private Boolean isLiveable;
    private Boolean isSuspended;
}
