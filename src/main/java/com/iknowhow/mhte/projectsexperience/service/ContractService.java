package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;

public interface ContractService {

    ContractDTO createNewContract(ContractDTO contract);

    ContractDTO updateContract(ContractDTO contractDTO);

    ContractDTO deleteContract(String id);
}
