package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsDTO {

    private Long id;
    private String message;
    private String username;
    private LocalDateTime date;
    private String role;
}
