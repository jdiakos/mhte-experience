package com.iknowhow.mhte.projectsexperience.dto;

import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.enums.ContractTypeEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractDTO {

    private Long id;
    @NotBlank
    private ContractTypeEnum contractType;
    @NotBlank
    private Double contractValue;
    private LocalDate signingDate;
    private ProjectDTO project;

}
