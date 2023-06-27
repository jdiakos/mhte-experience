package com.iknowhow.mhte.projectsexperience.dto;

import java.util.List;
import lombok.Data;

@Data
public class ProjectResponseDTO {

    private ProjectDescriptionDTO projectDescription;
    private ProjectFinancialElementsDTO projectFinancialElements;
    private List<ProjectContractorDTO> projectContractors;
    private List<ProjectSubcontractorDTO> projectSubcontractors;
    private List<ContractDTO> contracts;
    private List<ProjectDocumentsDTO> documents;
    private List<CommentsDTO> comments;

}
