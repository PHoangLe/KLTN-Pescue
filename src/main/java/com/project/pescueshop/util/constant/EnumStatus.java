package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumStatus {
    ACTIVE(1, "ACTIVE"),
    DELETED(2, "DELETED"),
    INACTIVE(3, "IN_ACTIVE"),
    LOCKED(4, "LOCKED"),
    HIDDEN(5, "HIDDEN");

    private static final Map<Integer, EnumStatus> BY_ID = new HashMap<>();
    private static final Map<String, EnumStatus> BY_LABEL = new HashMap<>();

    static {
        for (EnumStatus e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    private EnumStatus(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumStatus getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumStatus getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
