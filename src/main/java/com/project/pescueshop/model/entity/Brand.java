package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BRAND")
@Entity
@Name(prefix = "BRND", noun = "brand")
public class Brand {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String brandId;
    private String name;
    private String brandLogo;
}
