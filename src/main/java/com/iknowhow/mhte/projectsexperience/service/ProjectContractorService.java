package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractorResponseDTO;

import java.util.List;

public interface ProjectContractorService {

    List<ContractorResponseDTO> getAllContractorsForProject(Long projectId);

    void assignContractorToProject(ContractorDTO dto);


}
