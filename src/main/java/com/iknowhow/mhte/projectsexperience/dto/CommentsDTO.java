package com.iknowhow.mhte.projectsexperience.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsDTO {

    private Long id;
    @NotBlank
    private String message;
    private String username;
    private LocalDateTime date;
    private String role;
}
