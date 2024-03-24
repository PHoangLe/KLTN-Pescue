package com.project.pescueshop.util.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum EnumResponseCode {
    //<editor-fold desc="0-System">
    SYSTEM_ERROR("0_1_f", "System Error"),
    SUCCESS("0_2_s", "Succeed"),
    FAILED("0_3_f", "Failed"),
    MAIL_SENT_FAIL("0_4_f", "Mail sent failed"),
    //</editor-fold>

    //<editor-fold desc="1-AUTHENTICATION">
    CREATED_ACCOUNT_SUCCESSFUL("1_1_s", "Account has been created"),
    ACCOUNT_EXISTED("1_2_f", "Email existed"),
    ACCOUNT_LOCKED("1_3_f", "Account has been locked"),
    ACCOUNT_INACTIVE("1_4_f", "You must active your email before log in"),
    ACCOUNT_NOT_FOUND("1_5_f", "Account not found"),
    BAD_CREDENTIAL("1_6_f", "Credential error"),
    AUTHENTICATE_SUCCESSFUL("1_7_s", "Log in successfully"),
    NOT_LOGGED_IN("1_8_f", "Please log in"),
    USER_NOT_FOUND("1_9_f", "User Not Found"),
    OTP_INVALID("1_9_f", "Invalid Otp"),
    OTP_EXPIRED("1_9_f", "Otp expired"),
    OTP_INCORRECT("1_9_f", "Incorrect Otp"),
    //</editor-fold>

    //<editor-fold desc="2-PRODUCT">
    CREATED_CATEGORY_SUCCESSFUL("2_1_s", "New category has been added"),
    CREATED_SUBCATEGORY_SUCCESSFUL("2_2_f", "New subcategory has been added"),
    PRODUCT_NOT_FOUND("2_3_f", "Product not found"),
    ATTRIBUTE_EXISTED("2_4_f", "Attribute existed"),
    VARIETY_NOT_FOUND("2_5_f", "Variety not found"),
    CART_NOT_FOUND("2_6_f", "Cart not found"),
    CART_ITEM_NOT_FOUND("2_7_f", "Cart item not found"),
    //</editor-fold>

    //<editor-fold desc="3-IMPORT">
    IMPORT_INVOICE_NOT_FOUND("3_1_f", "Invoice does not exist"),
    //</editor-fold>

    //<editor-fold desc="4-USER">
    ADDRESS_NOT_FOUND("4_1_f", "Address not found"),
    //</editor-fold>

    //<editor-fold desc="5-INVOICE">
    INVOICE_NOT_FOUND("5_1_f", "Invoice not found"),
    //</editor-fold>
    //<editor-fold desc="6-VOUCHER_NOT_FOUND">
    VOUCHER_NOT_FOUND("6_1_f", "Voucher not found"),
    //</editor-fold>
    //<editor-fold desc="7-PAYMENT">
    NO_ITEM_TO_CHECKOUT("7_1_f", "No item to checkout"),
    //</editor-fold>
    //<editor-fold desc="8-PAYMENT">
    MERCHANT_NOT_FOUND("8_1_f", "Merchant not found"),
    MERCHANT_ALREADY_SUSPENDED("8_2_f", "Merchant already suspended"),
    MERCHANT_SUSPENDED("8_2_f", "Merchant suspended");
    //</editor-fold>

    private static final Map<String, EnumResponseCode> BY_STATUS_CODE = new HashMap<>();
    private static final Map<String, EnumResponseCode> BY_MESSAGE = new HashMap<>();

    static {
        for (EnumResponseCode e : values()) {
            BY_STATUS_CODE.put(e.statusCode, e);
            BY_MESSAGE.put(e.message, e);
        }
    }

    private final String statusCode;
    private final String message;

    private EnumResponseCode(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static EnumResponseCode getByStatusCode(String id) {
        return BY_STATUS_CODE.get(id);
    }

    public static EnumResponseCode getByMessage(String value) {
        return BY_MESSAGE.get(value);
    }
}
