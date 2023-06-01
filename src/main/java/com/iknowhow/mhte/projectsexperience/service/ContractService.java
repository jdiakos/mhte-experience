package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ContractProjectDTO;

public interface ContractService {

    ContractProjectDTO createNewContract(ContractProjectDTO contract);

    ContractProjectDTO updateContract(ContractProjectDTO contractDTO);

    ContractProjectDTO deleteContract(String id);
}
