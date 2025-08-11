package com.isacore.security.exception;

public class UsuarioUnicoException extends RuntimeException{
    private Integer errorCode;

    public UsuarioUnicoException(String message) {
        super(message);
    }

    public UsuarioUnicoException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        String fullMessage = (errorCode != null) ? "ErroCode: " + this.errorCode : "";
        return fullMessage + "\n" + super.getMessage();
    }
}
