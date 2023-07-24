package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.ContractRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import com.iknowhow.mhte.projectsexperience.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {

    private final FileNetService fileNetService;

    @Autowired
    public ContractServiceImpl(FileNetService fileNetService) {
        this.fileNetService = fileNetService;
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
//            	List<Long> ids = project.getContracts().stream().map(Contract::getId).toList();
//    			if (ids.contains(dtoList.get(i).getId())) {
//                    log.error("ERROR CONTRACT " + dtoList.get(i).getFilename());
//    				throw new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACT_ALREADY_ASSIGNED);
//    			}
    			contract.setId(dtoList.get(i).getId());
            }
            contract.setContractType(dtoList.get(i).getContractType());
            contract.setContractValue(dtoList.get(i).getContractValue());
            contract.setSigningDate(dtoList.get(i).getSigningDate());
            contract.setProject(project);
            contract.setDateCreated(LocalDateTime.now());
            contract.setLastModifiedBy(userPrincipal.getUsername());
            if (dtoList.get(i).getContractGUID() != null) {
                contract.setContractGUID(dtoList.get(i).getContractGUID());
                contract.setFilename(dtoList.get(i).getFilename());
            } else {
            	if(contractFiles==null || contractFiles.length<=fileIndex) {
            		throw new MhteProjectsNotFoundException(MhteProjectErrorMessage.MISSING_FILE);
            	}
                contract.setContractGUID(
                		fileNetService.uploadFileToFilenet(project, contractFiles[fileIndex], userPrincipal.getUsername())
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