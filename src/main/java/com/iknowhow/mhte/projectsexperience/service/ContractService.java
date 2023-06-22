package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContractService {

    List<Contract> assignContractsToProject(List<ContractDTO> dtoList,
                                            MultipartFile[] contractFiles,
                                            Project project,
                                            MhteUserPrincipal userPrincipal);

}
