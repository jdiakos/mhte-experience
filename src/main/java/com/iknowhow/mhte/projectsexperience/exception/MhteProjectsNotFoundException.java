package com.iknowhow.mhte.projectsexperience.exception;

public class MhteProjectsNotFoundException  extends RuntimeException {

    public MhteProjectsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MhteProjectsNotFoundException(String message) {
        super(message);
    }

    public MhteProjectsNotFoundException(MhteProjectErrorMessage message) {
        super(message.toString());
    }

    public MhteProjectsNotFoundException(Throwable cause) {
        super(cause);
    }
}
