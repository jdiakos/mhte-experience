package com.iknowhow.mhte.projectsexperience.dto;

import java.util.List;
import lombok.Data;

@Data
public class ProjectResponseDTO {

    private ProjectDescriptionDTO projectDescription;
    private ProjectFinancialElementsDTO projectFinancialElements;
    private List<ProjectContractorResponseDTO> projectContractors;
    private List<ProjectSubcontractorResponseDTO> projectSubcontractors;
    private List<ContractResponseDTO> contracts;
    private List<ProjectDocumentsResponseDTO> documents;
    private List<CommentsResponseDTO> comments;

}
