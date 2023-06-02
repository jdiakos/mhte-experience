package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ContractProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;

import java.util.List;

public interface ContractService {

    ContractProjectDTO createNewContract(ContractProjectDTO contract);

    ContractProjectDTO updateContract(ContractProjectDTO contractDTO);

    ContractDTO deleteContract(Long id);

    List<ContractResponseDTO> getAllContractsByProject(Long projectId);
}
