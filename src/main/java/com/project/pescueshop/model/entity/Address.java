package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "ADDRESS")
@Entity
@AllArgsConstructor
@Builder
@Name(prefix = "ADDR")
public class Address {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String addressId;
    private String userId;
    private String streetName;
    private String wardName;
    private String wardCode;
    private String districtName;
    private Integer districtId;
    private String cityName;
    private String status;
}
