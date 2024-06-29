package com.project.pescueshop.model.elastic;

import java.util.HashMap;
import java.util.Map;

public class Index {
    public enum Name {
        INVOICE_DATA
    }

    private final static Map<Name, String> map;

    static {
        map = new HashMap<>();
        map.put(Name.INVOICE_DATA, "invoice_data_idx");
    }

    public static String getIndexName(Name name) {
        return map.get(name);
    }
}
