package com.isacore.security.exception;

public class RecursoNotFoundException extends RuntimeException{
    private Integer errorCode;

    public RecursoNotFoundException(String message) {
        super(message);
    }

    public RecursoNotFoundException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        String fullMessage = (errorCode != null) ? "ErroCode: " + this.errorCode : "";
        return fullMessage + "\n" + super.getMessage();
    }
}
