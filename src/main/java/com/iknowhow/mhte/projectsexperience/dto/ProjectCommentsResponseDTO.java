package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectCommentsResponseDTO extends ProjectCommentsDTO {

    private Long id;
    private String username;
    private LocalDateTime date;
    private String role;
}
