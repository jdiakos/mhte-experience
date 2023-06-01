package com.iknowhow.mhte.projectsexperience.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MhteProjectExceptionHandler {
	
    @ExceptionHandler({MhteProjectsNotFoundException.class})
    public ResponseEntity<MhteProjectErrorDTO> handleInternalError(MhteProjectsNotFoundException ex) {
    	MhteProjectErrorDTO response = new MhteProjectErrorDTO();
	    response.setMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
