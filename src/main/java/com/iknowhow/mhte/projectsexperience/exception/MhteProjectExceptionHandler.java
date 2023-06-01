package com.iknowhow.mhte.projectsexperience.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

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
     * VALIDATION EXCEPTIONS
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<MhteProjectErrorDTO> handleFieldValidationException(MethodArgumentNotValidException ex) {
        MhteProjectErrorDTO response = new MhteProjectErrorDTO();

        response.setMessage(MhteProjectErrorMessage.CONSTRAINT_VALIDATION_ERROR.name());
        response.setErrors(new ArrayList<>());
        ex.getFieldErrors().forEach(
                error -> response.getErrors().add(String.format(
                        "%s %s",
                        error.getField(),
                        error.getDefaultMessage()))
        );


        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
