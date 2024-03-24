package com.project.pescueshop.util.constant;

import java.util.HashMap;
import java.util.Map;

public enum EnumRoleId {
    ADMIN(1, "ROLE_ADMIN"),
    MERCHANT(2, "ROLE_MERCHANT"),
    CUSTOMER(3, "ROLE_CUSTOMER");

    private static final Map<Integer, EnumRoleId> BY_ID = new HashMap<>();
    private static final Map<String, EnumRoleId> BY_LABEL = new HashMap<>();

    static {
        for (EnumRoleId e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    private EnumRoleId(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumRoleId getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumRoleId getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
