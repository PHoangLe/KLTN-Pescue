package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumOtpType {
    CONFIRM_EMAIL(1, "CONFIRM_EMAIL"),
    FORGOT_PASSWORD(2, "FORGOT_PASSWORD");

    private static final Map<Integer, EnumOtpType> BY_ID = new HashMap<>();
    private static final Map<String, EnumOtpType> BY_LABEL = new HashMap<>();

    static {
        for (EnumOtpType e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    EnumOtpType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumOtpType getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumOtpType getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
