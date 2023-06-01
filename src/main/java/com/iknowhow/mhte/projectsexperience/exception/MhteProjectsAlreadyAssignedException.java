package com.iknowhow.mhte.projectsexperience.exception;

public class MhteProjectsAlreadyAssignedException extends RuntimeException {

    public MhteProjectsAlreadyAssignedException() {
        super();
    }

    public MhteProjectsAlreadyAssignedException(String message) {
        super(message);
    }

    public MhteProjectsAlreadyAssignedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MhteProjectsAlreadyAssignedException(Throwable cause) {
        super(cause);
    }
}
