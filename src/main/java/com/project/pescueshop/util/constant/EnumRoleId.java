package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumRoleId {
    ADMIN("ROLE_1", "ROLE_ADMIN"),
    MERCHANT("ROLE_2", "ROLE_MERCHANT"),
    CUSTOMER("ROLE_3", "ROLE_CUSTOMER");

    private static final Map<String, EnumRoleId> BY_ID = new HashMap<>();
    private static final Map<String, EnumRoleId> BY_LABEL = new HashMap<>();

    static {
        for (EnumRoleId e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final String id;
    private final String value;

    private EnumRoleId(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumRoleId getById(String id) {
        return BY_ID.get(id);
    }

    public static EnumRoleId getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
