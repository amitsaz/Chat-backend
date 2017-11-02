package com.chat.exceptions;

/**
 * Created by asazawal on 10/21/17.
 * Error codes that the client can interpret and takes action accordingly
 */
public enum ErrorCodes {

    USER_ALREADY_EXISTS(1001,"User already exists.Please try a new one !"),
    USER_NOT_FOUND(1002,"This username does not exist in system.");

    private String message;
    private int code;

    private ErrorCodes(int code,String externalKey) {
        this.message = externalKey;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
