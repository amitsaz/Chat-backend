package com.chat.exceptions;

/**
 * Created by asazawal on 10/21/17.
 * Thrown when trying to retrieve messages for an unknown user
 */
public class UserNotFoundException extends Throwable {

    private int code;
    public UserNotFoundException() {
        this(500);
    }
    public UserNotFoundException(ErrorCodes errorCodes){
        this(errorCodes.getCode(),errorCodes.getMessage());
    }
    public UserNotFoundException(int code) {
        this(code, "Error while processing the request", null);
    }
    public UserNotFoundException(int code, String message) {
        this(code, message, null);
    }
    public UserNotFoundException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
