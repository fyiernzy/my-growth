package com.ifast.ipaymy.backend.util.cryptographic.exception;

public class CryptographicException extends RuntimeException {

    public CryptographicException(String message, Throwable e) {
        super(message, e);
    }
}
