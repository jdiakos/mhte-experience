package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.ContractRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import com.iknowhow.mhte.projectsexperience.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public List<Contract> assignContractsToProject(List<ContractDTO> dtoList,
                                          MultipartFile[] contractFiles,
                                          Project project,
                                          MhteUserPrincipal userPrincipal) {
        List<Contract> contracts = new ArrayList<>();
        int fileIndex=0;
        for (int i = 0; i < dtoList.size(); i++) {
            Contract contract = new Contract();
            if (dtoList.get(i).getId() != null) {
            	List<Long> ids = project.getContracts().stream().map(Contract::getId).toList();
    			if (ids.contains(dtoList.get(i).getId())) {
    				throw new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACTOR_ALREADY_ASSIGNED);
    			}
    			contract.setId(dtoList.get(i).getId());
            }
            contract.setContractType(dtoList.get(i).getContractType());
            contract.setContractValue(dtoList.get(i).getContractValue());
            contract.setSigningDate(dtoList.get(i).getSigningDate());
            contract.setProject(project);
            contract.setDateCreated(LocalDateTime.now());
            // @TODO - PLACEHOLDER: CHANGE WITH USER PRINCIPAL
            contract.setLastModifiedBy("JULIUS CAESAR");
//          contract.setLastModifiedBy(userPrincipal.getUsername());
            if (dtoList.get(i).getContractGUID() != null) {
                contract.setContractGUID(dtoList.get(i).getContractGUID());
                contract.setFilename(dtoList.get(i).getFilename());
            } else {
            	if(contractFiles==null || contractFiles.length<=fileIndex) {
            		throw new MhteProjectsNotFoundException(MhteProjectErrorMessage.MISSING_FILE);
            	}
                contract.setContractGUID(
                		fileNetService.uploadFileToFilenet(project, contractFiles[fileIndex], "ASTERIX")
                );
                contract.setFilename(contractFiles[fileIndex].getOriginalFilename());
                fileIndex++;
            }
            contracts.add(contract);

        }
        if(project.getId()!=null) {
        	project.getContracts().clear();
        }
        return contracts;

    }

}