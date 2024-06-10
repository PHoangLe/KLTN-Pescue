package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumLiveStatus {
    ACTIVE(1, "ACTIVE"),
    TERMINATED(2, "TERMINATED"),
    DONE(3, "DONE");

    private static final Map<Integer, EnumLiveStatus> BY_ID = new HashMap<>();
    private static final Map<String, EnumLiveStatus> BY_LABEL = new HashMap<>();

    static {
        for (EnumLiveStatus e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    EnumLiveStatus(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumLiveStatus getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumLiveStatus getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
