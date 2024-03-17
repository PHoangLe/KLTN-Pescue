package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumPaymentType {
    CREDIT_CARD(1, "CREDIT_CARD"),
    COD(2, "COD");

    private static final Map<Integer, EnumPaymentType> BY_ID = new HashMap<>();
    private static final Map<String, EnumPaymentType> BY_LABEL = new HashMap<>();

    static {
        for (EnumPaymentType e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    private EnumPaymentType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumPaymentType getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumPaymentType getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
