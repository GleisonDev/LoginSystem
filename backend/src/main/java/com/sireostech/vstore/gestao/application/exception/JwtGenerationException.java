package com.sireostech.vstore.gestao.application.exception;

public class JwtGenerationException extends RuntimeException {

    public JwtGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtGenerationException(String message) {
        super(message);
    }
}