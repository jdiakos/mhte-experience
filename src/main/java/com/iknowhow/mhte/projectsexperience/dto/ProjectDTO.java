package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class ProjectDTO {

	@NotNull
	@Valid
    private ProjectDescriptionDTO projectDescription;
    private ProjectFinancialElementsDTO projectFinancialElements;
    private List<ContractDTO> contracts;
    private List<ProjectContractorDTO> projectContractors;
    private List<ProjectSubcontractorDTO> projectSubcontractors;
    private List<CommentsDTO> projectComments;
    private List<ProjectDocumentsDTO> projectDocuments;

}
