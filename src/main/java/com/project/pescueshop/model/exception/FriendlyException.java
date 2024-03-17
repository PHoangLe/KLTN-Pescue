package com.project.pescueshop.model.exception;

import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.Getter;

@Getter
public class FriendlyException extends Exception{
    private EnumResponseCode statusCode;

    public FriendlyException(String message) {
        super(message);
    }
    public FriendlyException(EnumResponseCode enumResponseCode){
        super(enumResponseCode.getMessage());
        this.statusCode = enumResponseCode;
    }
}
