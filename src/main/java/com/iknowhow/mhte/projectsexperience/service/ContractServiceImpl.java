package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.ContractRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import com.iknowhow.mhte.projectsexperience.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final FileNetService fileNetService;
    private final Utils utils;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository,
                               FileNetService fileNetService,
                               Utils utils) {
        this.contractRepository = contractRepository;
        this.fileNetService = fileNetService;
        this.utils = utils;
    }

    @Override
    @Transactional
    public List<Contract> createContracts(List<ContractDTO> dtoList,
                                          MultipartFile[] contractFiles,
                                          Project project,
                                          MhteUserPrincipal userPrincipal) {
        List<Contract> contracts = new ArrayList<>();

        for (int i = 0; i < dtoList.size(); i++) {
            Contract contract = new Contract();
            contract.setContractType(dtoList.get(i).getContractType());
            contract.setContractValue(dtoList.get(i).getContractValue());
            contract.setSigningDate(dtoList.get(i).getSigningDate());
            contract.setProject(project);

            contract.setDateCreated(LocalDateTime.now());
            //@TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
            contract.setLastModifiedBy("JULIUS CAESAR");
//          contract.setLastModifiedBy(userPrincipal.getUsername());

            if (dtoList.get(i).getContractGUID() != null) {
                contract.setContractGUID(dtoList.get(i).getContractGUID());
                contract.setFilename(contractFiles[i].getOriginalFilename());
            } else {
                contract.setContractGUID(
                        fileNetService.uploadFileToFilenet(project, contractFiles[i], "ASTERIX")
                );
                contract.setFilename(contractFiles[i].getOriginalFilename());
            }
            contracts.add(contract);

        }

        return contracts;

    }

}