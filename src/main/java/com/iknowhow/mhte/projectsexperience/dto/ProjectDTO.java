package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
public class ProjectDTO {

	@NotNull
	@Valid
    private ProjectDescriptionDTO projectDescription;
    private ProjectFinancialElementsDTO projectFinancialElements;
    @Valid
    private List<ContractDTO> contracts;
    @Valid
    private List<ProjectContractorDTO> projectContractors;
    @Valid
    private List<ProjectSubcontractorDTO> projectSubcontractors;
    @NotNull
    private List<CommentsDTO> projectComments;
    private List<ProjectDocumentsDTO> projectDocuments;

}
