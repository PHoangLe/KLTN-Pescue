package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumGroupType {
    DAY(1, "DAY", "day"),
    MONTH(2, "MONTH", "month"),
    YEAR(3, "YEAR", "year");

    private static final Map<Integer, EnumGroupType> BY_ID = new HashMap<>();
    private static final Map<String, EnumGroupType> BY_LABEL = new HashMap<>();
    private static final Map<String, EnumGroupType> BY_GRANULARITY = new HashMap<>();

    static {
        for (EnumGroupType e : values()) {
            BY_ID.put(e.id, e);
            BY_LABEL.put(e.value, e);
            BY_GRANULARITY.put(e.granularity, e);
        }
    }

    private final Integer id;
    private final String value;
    private final String granularity;

    EnumGroupType(Integer id, String value, String granularity) {
        this.id = id;
        this.value = value;
        this.granularity = granularity;
    }

    public static EnumGroupType getById(Integer id) {
        return BY_ID.get(id);
    }

    public static EnumGroupType getByValue(String value) {
        return BY_LABEL.get(value);
    }
}
