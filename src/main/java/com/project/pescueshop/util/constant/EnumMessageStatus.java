package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumMessageStatus {
    RECEIVED(1, "RECEIVED"),
    DELIVERED(2, "DELIVERED");

    private static final Map<Integer, EnumMessageStatus> BY_ID = new HashMap<>();
    private static final Map<String, EnumMessageStatus> BY_LABEL = new HashMap<>();

    static {
        for (EnumMessageStatus e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    EnumMessageStatus(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumMessageStatus getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumMessageStatus getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
