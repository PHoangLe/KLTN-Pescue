package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.dto.VarietyDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VARIETY")
@Entity
@Name(prefix = "VARI")
@Builder
public class Variety {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.project.pescueshop.util.CustomIdGenerator")
    private String varietyId;
    private String productId;
    private String name;
    private long price;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "VARIETIES_ATTRIBUTES",
            joinColumns = @JoinColumn(name = "varietyId", referencedColumnName = "varietyId"),
            inverseJoinColumns = @JoinColumn(name = "attributeId", referencedColumnName = "attributeId")
    )
    private List<VarietyAttribute> varietyAttributes;
    private String status;
    private Integer stockAmount = 0;

    public Variety(VarietyDTO dto){
        this.varietyId = dto.getVarietyId();
        this.productId = dto.getProductId();
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.varietyAttributes = dto.getVarietyAttributes();
        this.status = dto.getStatus();
        this.stockAmount = 0;
    }

    public Variety(String productId, String name, Long price, String status){
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.status = status;
    }

    public void addAttribute(VarietyAttribute attribute){
        if (this.varietyAttributes == null){
            this.varietyAttributes = new ArrayList<>();
        }

        this.varietyAttributes.add(attribute);
    }
}
