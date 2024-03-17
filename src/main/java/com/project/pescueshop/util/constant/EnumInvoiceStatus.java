package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumInvoiceStatus {
    PENDING(1, "PENDING"),
    COMPLETED(2, "COMPLETED"),
    CANCEL(3, "CANCEL"),
    PAID(4, "PAID"),
    RETURN(5, "RETURN");

    private static final Map<Integer, EnumInvoiceStatus> BY_ID = new HashMap<>();
    private static final Map<String, EnumInvoiceStatus> BY_LABEL = new HashMap<>();

    static {
        for (EnumInvoiceStatus e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    EnumInvoiceStatus(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumInvoiceStatus getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumInvoiceStatus getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
