package com.iknowhow.mhte.projectsexperience.dto;

import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectDescriptionDTO {

    private Long id;
    @NotNull
    private ProjectsCategoryEnum projectCategory;
    private String adam;
    @NotBlank
    private String protocolNumber;
    @NotBlank
    private String title;
    private String description;
    @NotBlank
    private String responsibleEntity;
    @NotBlank
    private String contractingAuthority;
    @NotBlank
    private String headAuthority;


}
