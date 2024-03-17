package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumPetType {
    ACTIVE(1, "DOG"),
    DELETED(2, "CAT");

    private static final Map<Integer, EnumPetType> BY_ID = new HashMap<>();
    private static final Map<String, EnumPetType> BY_LABEL = new HashMap<>();

    static {
        for (EnumPetType e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
        }
    }

    private final Integer id;
    private final String value;

    private EnumPetType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static EnumPetType getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumPetType getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
