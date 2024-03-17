package com.project.pescueshop.util.constant;

import java.util.HashMap;
import java.util.Map;

public enum EnumVarietyAttributeType {
    SIZE(1, "SIZE"),
    COLOR(2, "COLOR");

    private static final Map<Integer, EnumVarietyAttributeType> BY_ID = new HashMap<>();
    private static final Map<String, EnumVarietyAttributeType> BY_LABEL = new HashMap<>();

    static {
        for (EnumVarietyAttributeType e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    private EnumVarietyAttributeType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumVarietyAttributeType getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumVarietyAttributeType getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
