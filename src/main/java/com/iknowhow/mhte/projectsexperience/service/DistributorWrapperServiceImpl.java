package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ProjectMasterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributorWrapperServiceImpl implements DistributorWrapperService {

    private final ProjectService projectService;
    private final ContractService contractService;
    private final ProjectContractorService projectContractorService;
    private final ProjectSubcontractorService projectSubcontractorService;

    @Autowired
    public DistributorWrapperServiceImpl(ProjectService projectService,
                                         ContractService contractService,
                                         ProjectContractorService projectContractorService,
                                         ProjectSubcontractorService projectSubcontractorService) {
        this.projectService = projectService;
        this.contractService = contractService;
        this.projectContractorService = projectContractorService;
        this.projectSubcontractorService = projectSubcontractorService;
    }

    @Override
    public void createProject(ProjectMasterDTO dto) {

    }
}
