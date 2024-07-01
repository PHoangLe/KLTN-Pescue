package com.project.pescueshop.model.exception;

import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.Getter;

@Getter
public class FriendlyException extends Exception{
    private EnumResponseCode statusCode;

    public FriendlyException(String message) {
        super(message);
        this.statusCode = EnumResponseCode.BAD_REQUEST;
    }
    public FriendlyException(EnumResponseCode enumResponseCode){
        super(enumResponseCode.getMessage());
        this.statusCode = enumResponseCode;
    }

    public FriendlyException(String message, Throwable cause) {
        super(message, cause);
    }
}
