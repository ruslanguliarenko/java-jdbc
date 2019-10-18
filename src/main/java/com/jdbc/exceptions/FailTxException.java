package com.jdbc.exceptions;

public class FailTxException extends RuntimeException {
    public FailTxException(String message){
        super(message);
    }
}
