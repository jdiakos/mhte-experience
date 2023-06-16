package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.dto.*;
import com.iknowhow.mhte.projectsexperience.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributorWrapperServiceImpl implements DistributorWrapperService {

    private final ProjectService projectService;
    private final ContractService contractService;
    private final ProjectContractorService projectContractorService;
    private final ProjectSubcontractorService projectSubcontractorService;
    private final Utils utils;

    @Autowired
    public DistributorWrapperServiceImpl(ProjectService projectService,
                                         ContractService contractService,
                                         ProjectContractorService projectContractorService,
                                         ProjectSubcontractorService projectSubcontractorService,
                                         Utils utils) {
        this.projectService = projectService;
        this.contractService = contractService;
        this.projectContractorService = projectContractorService;
        this.projectSubcontractorService = projectSubcontractorService;
        this.utils = utils;
    }

    @Override
    public void createProject(ProjectMasterDTO dto, MhteUserPrincipal userPrincipal) {

        // call project service
        CUDProjectDTO cudProjectDTO = utils.initModelMapperStrict().map(dto, CUDProjectDTO.class);
        Project project = projectService.addNewProject(cudProjectDTO);

        // call project contractor service
        List<ProjectContractorDTO> projectContractorDTOList = dto.getProjectContractors();
        projectContractorDTOList.forEach(
                contractor -> contractor.setProjectId(project.getId())
        );
        projectContractorDTOList.forEach(
                contractor -> projectContractorService.assignContractorToProject(contractor, userPrincipal)
        );

        // call project subcontractor service
        // @TODO - SAVE FILES
        List<ProjectSubcontractorDTO> subcontractorDTOList = dto.getProjectSubcontractors();
        subcontractorDTOList.forEach(
                subcontractor -> subcontractor.setProjectId(project.getId())
        );
        subcontractorDTOList.forEach(
                subcontractor -> projectSubcontractorService.assignSubcontractorToProject(subcontractor, userPrincipal)
        );

        // call contract service
        // @TODO -- SAVE FILES
        List<ContractDTO> contractDTOList = dto.getContracts();
        contractDTOList.forEach(
                contract -> contract.setProjectId(project.getId())
        );
        contractDTOList.forEach(contractService::createNewContract);

    }
}
