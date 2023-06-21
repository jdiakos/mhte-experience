package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContractService {

    List<Contract> createContracts(List<ContractDTO> dtoList,
                                   MultipartFile[] contractFiles,
                                   Project project,
                                   MhteUserPrincipal userPrincipal);

    List<ContractResponseDTO> getAllContractsByProject(Long projectId);

    Page<ContractDTO> fetchAllContractsPaginated(Pageable page);

    // @TODO - MOVE TO PROJECT SERVICE
    void uploadFile(ContractDTO contract, MultipartFile file, String username);

    // @TODO - MOVE TO PROJECT SERVICE
    DownloadFileDTO downloadFile(String guid);

    // @TODO - MOVE TO PROJECT SERVICE
    void deleteFile(Long contractId, String guid, MhteUserPrincipal userPrincipal);

}
