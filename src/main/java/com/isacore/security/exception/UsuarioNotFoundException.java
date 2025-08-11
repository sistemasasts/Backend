package com.isacore.security.exception;

public class UsuarioNotFoundException extends RuntimeException{
    private Integer errorCode;

    public UsuarioNotFoundException(String message) {
        super(message);
    }

    public UsuarioNotFoundException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        String fullMessage = (errorCode != null) ? "ErroCode: " + this.errorCode : "";
        return fullMessage + "\n" + super.getMessage();
    }
}
