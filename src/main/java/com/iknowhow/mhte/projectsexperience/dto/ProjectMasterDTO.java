package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class ProjectMasterDTO {

	@NotNull
	@Valid
    private ProjectDescriptionDTO projectDescription;
	@NotNull
	@Valid
    private ProjectFinancialElementsDTO projectFinancialElements;
    @NotEmpty
    @Valid
    private List<ContractDTO> contracts;
    @NotEmpty
    @Valid
    private List<ProjectContractorDTO> projectContractors;
    @NotNull
    @Valid
    private List<ProjectSubcontractorDTO> projectSubcontractors;
    @NotNull
    private List<CommentsDTO> projectComments;

}
