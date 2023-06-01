package com.iknowhow.mhte.projectsexperience.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MhteProjectExceptionHandler {

    Logger log = LoggerFactory.getLogger(MhteProjectExceptionHandler.class);

    /**
     * CRUD EXCEPTIONS
     */
    @ExceptionHandler({MhteProjectsNotFoundException.class})
    public ResponseEntity<MhteProjectErrorDTO> handleInternalError(MhteProjectsNotFoundException ex) {
    	MhteProjectErrorDTO response = new MhteProjectErrorDTO();
	    response.setMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * GENERIC EXCEPTIONS
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<MhteProjectErrorDTO> handleGenericException(Exception ex) {
        log.error(ex.getMessage());

        MhteProjectErrorDTO response = new MhteProjectErrorDTO();
        response.setMessage(" There was an error. Please contact your administrator");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
