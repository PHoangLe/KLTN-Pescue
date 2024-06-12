package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumCartStatus {
    ACTIVE(1, "ACTIVE"),
    DELETED(2, "DELETED");

    private static final Map<Integer, EnumCartStatus> BY_ID = new HashMap<>();
    private static final Map<String, EnumCartStatus> BY_LABEL = new HashMap<>();

    static {
        for (EnumCartStatus e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    private EnumCartStatus(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumCartStatus getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumCartStatus getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
