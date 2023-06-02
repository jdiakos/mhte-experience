package com.iknowhow.mhte.projectsexperience.exception;

public class MhteProjectCustomValidationException extends RuntimeException {
    public MhteProjectCustomValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MhteProjectCustomValidationException(String message) {
        super(message);
    }

    public MhteProjectCustomValidationException(MhteProjectErrorMessage message) {
        super(message.toString());
    }

    public MhteProjectCustomValidationException(Throwable cause) {
        super(cause);
    }
}
