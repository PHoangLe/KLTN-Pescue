package com.project.pescueshop.model.dto.general;

import com.project.pescueshop.model.annotation.Name;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T>{
    private MetaData meta;
    private Map<String, T> data;

    public void setMeta(EnumResponseCode enumResponseCode){
        this.meta.setStatusCode(enumResponseCode.getStatusCode());
        this.meta.setMessage(enumResponseCode.getMessage());
    }

    public ResponseDTO(EnumResponseCode enumResponseCode, T data){
        this.meta = new MetaData();
        this.data = new HashMap<>();
        this.setMeta(enumResponseCode);
        this.data.put(extractFieldName(data), data);
    }

    public ResponseDTO(EnumResponseCode enumResponseCode, T data, String name){
        this.meta = new MetaData();
        this.data = new HashMap<>();
        this.setMeta(enumResponseCode);
        this.data.put(name, data);
    }

    public ResponseDTO(EnumResponseCode enumResponseCode){
        this.meta = new MetaData();
        this.setMeta(enumResponseCode);
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class MetaData {
        private String statusCode;
        private String message;
    }

    private String extractFieldName(T data) {
        Class<?> clazz = data.getClass();
        Name annotation = clazz.getAnnotation(Name.class);
        if (annotation != null) {
            return annotation.noun();
        }
        return "data";
    }
}
