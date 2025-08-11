package com.isacore.security.exception;

public class UsuarioPerfilNotFoundException extends RuntimeException{
    private Integer errorCode;

    public UsuarioPerfilNotFoundException(String message) {
        super(message);
    }

    public UsuarioPerfilNotFoundException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        String fullMessage = (errorCode != null) ? "ErroCode: " + this.errorCode : "";
        return fullMessage + "\n" + super.getMessage();
    }
}
