package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsResponseDTO extends CommentsDTO {

    private Long id;
    private String username;
    private LocalDateTime date;
    private String role;
}
