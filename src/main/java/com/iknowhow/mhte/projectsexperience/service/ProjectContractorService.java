package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ContractorDTO;

import java.util.List;

public interface ProjectContractorService {

    List<ContractorDTO> getAllContractorsForProject(Long projectId);

    void assignContractorToProject(ContractorDTO dto);


}
