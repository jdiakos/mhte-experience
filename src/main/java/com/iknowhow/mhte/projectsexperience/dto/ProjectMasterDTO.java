package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectMasterDTO {

    private ProjectDescriptionDTO projectDescription;
    private ProjectFinancialElementsDTO financialElements;
    private List<ContractDTO> contracts;
    private List<ProjectContractorDTO> projectContractors;
    private List<ProjectSubcontractorDTO> projectSubcontractors;
    private List<CommentsDTO> projectComments;

}
