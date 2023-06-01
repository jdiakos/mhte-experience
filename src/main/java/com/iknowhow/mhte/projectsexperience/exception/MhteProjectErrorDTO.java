package com.iknowhow.mhte.projectsexperience.exception;

import java.util.List;

import lombok.Data;

@Data
public class MhteProjectErrorDTO {

    private String message;
    private List<String> errors;
    
}
