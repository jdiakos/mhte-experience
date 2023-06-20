package com.iknowhow.mhte.projectsexperience.dto;

import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import lombok.Data;

@Data
public class ProjectDescriptionDTO {

	private Long id;
    private ProjectsCategoryEnum projectCategory;
    private String adam;
    private String protocolNumber;
    private String title;
    private String description;
    private String responsibleEntity;
    private String contractingAuthority;
    private String headAuthority;


}
