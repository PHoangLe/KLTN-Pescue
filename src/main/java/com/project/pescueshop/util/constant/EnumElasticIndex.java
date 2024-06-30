package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumElasticIndex {
    INVOICE_DATA("invoice_data"),
    PRODUCT_DATA("product_data"),
    MERCHANT_DATA("merchant_data"),
    VIEW_AUDIT_LOG("views_audit_log_data"),
    RATING_DATA("rating_data");

    private static final Map<String, EnumElasticIndex> BY_NAME = new HashMap<>();

    static {
        for (EnumElasticIndex e : values()) {
            BY_NAME.put(e.name, e);
        }
    }

    private final String name;

    private EnumElasticIndex(String name) {
        this.name = name;
    }


    public static EnumElasticIndex getByValue(String value) {
        return BY_NAME.get(value);
    }
}
