package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.dto.ContractProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContractService {

    ContractProjectDTO createNewContract(ContractProjectDTO contract);

    ContractProjectDTO updateContract(ContractProjectDTO contractDTO);

    ContractProjectDTO deleteContract(Long id);

    List<ContractResponseDTO> getAllContractsByProject(Long projectId);

    Page<ContractProjectDTO> fetchAllContractsPaginated(Pageable page);
    
    void uploadFile(ContractProjectDTO contract, MultipartFile document);
}
