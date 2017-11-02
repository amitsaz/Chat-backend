package com.chat.exceptions;

/**
 * Created by asazawal on 10/21/17.
 * Thrown when trying to add a user with existing username
 */
public class UserAlreadyExistsException extends Throwable {

    private int code;
    public UserAlreadyExistsException() {
        this(500);
    }
    public UserAlreadyExistsException(ErrorCodes errorCodes){
        this(errorCodes.getCode(),errorCodes.getMessage());
    }
    public UserAlreadyExistsException(int code) {
        this(code, "Error while processing the request", null);
    }
    public UserAlreadyExistsException(int code, String message) {
        this(code, message, null);
    }
    public UserAlreadyExistsException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
