package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.ContractProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
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
    
    void uploadFile(ContractProjectDTO contract, MultipartFile document, String username);

    DownloadFileDTO downloadFile(String guid);

    void deleteFile(Long contractId, String guid);

}
