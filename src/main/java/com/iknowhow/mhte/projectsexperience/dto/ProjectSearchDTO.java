package com.iknowhow.mhte.projectsexperience.dto;

import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import lombok.Data;

@Data
public class ProjectSearchDTO {
    private ProjectsCategoryEnum projectCategory;
    private String adam;
    private String protocolNumber;
    private String responsibleEntity;
}
