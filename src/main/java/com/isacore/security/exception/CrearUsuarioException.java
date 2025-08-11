package com.isacore.security.exception;

public class CrearUsuarioException extends RuntimeException{
    private Integer errorCode;
    private final String detailedMessage;

    public CrearUsuarioException(String message, Throwable throwable) {
        super(message, throwable);
        this.detailedMessage = throwable.getMessage();
    }

    public CrearUsuarioException(String message, Integer errorCode, Throwable throwable) {
        super(message);
        this.errorCode = errorCode;
        this.detailedMessage = throwable.getMessage();
    }

    @Override
    public String getMessage() {
        String fullMessage = (errorCode != null) ? "ErroCode: " + this.errorCode : "";
        return fullMessage + "\n" + super.getMessage() + "\n Detailed Message: "+this.detailedMessage;
    }
}
