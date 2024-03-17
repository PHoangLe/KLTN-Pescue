package com.project.pescueshop.model.exception;

import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.Getter;

@Getter
public class UnauthenticatedException extends FriendlyException{
    private String email;

    public UnauthenticatedException(String message) {
        super(message);
    }

    public UnauthenticatedException(EnumResponseCode enumResponseCode) {
        super(enumResponseCode);
    }

    public UnauthenticatedException(EnumResponseCode enumResponseCode, String email) {
        super(enumResponseCode);
        this.email = email;
    }
}
