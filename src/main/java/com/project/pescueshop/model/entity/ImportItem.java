package com.project.pescueshop.model.entity;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.model.key.ImportItemKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "IMPORT_ITEM")
@Entity
@Name(prefix = "IMIT")
@IdClass(ImportItemKey.class)
public class ImportItem {
    @Id
    private String importInvoiceId;
    @Id
    private String varietyId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "varietyId", referencedColumnName = "varietyId", insertable = false, updatable = false)
    private Variety variety;
    private Integer quantity;
    private Long importPrice;
    private Long totalImportPrice;
}
