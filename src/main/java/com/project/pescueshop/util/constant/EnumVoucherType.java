package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumVoucherType {
    PERCENTAGE(1, "PERCENTAGE"),
    FLAT(2, "FLAT");

    private static final Map<Integer, EnumVoucherType> BY_ID = new HashMap<>();
    private static final Map<String, EnumVoucherType> BY_LABEL = new HashMap<>();

    static {
        for (EnumVoucherType e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    private EnumVoucherType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumVoucherType getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumVoucherType getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
