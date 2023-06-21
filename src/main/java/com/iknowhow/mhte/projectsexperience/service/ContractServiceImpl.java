package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.ContractRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import com.iknowhow.mhte.projectsexperience.utils.Utils;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ProjectRepository projectRepository;
    private final FileNetService fileNetService;
    private final Utils utils;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository,
                               ProjectRepository projectRepository,
                               FileNetService fileNetService,
                               Utils utils) {
        this.contractRepository = contractRepository;
        this.projectRepository = projectRepository;
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

    @Override
    public List<ContractResponseDTO> getAllContractsByProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
        );

        return contractRepository.findAllByProjectId(projectId)
                .stream()
                .map(this::toContractResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ContractDTO> fetchAllContractsPaginated(Pageable page) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return contractRepository.findAll(page).map(contract -> modelMapper.map(contract, ContractDTO.class));
    }

    @Override
    @Transactional
    public void uploadFile(ContractDTO dto, MultipartFile file, String username) {
        Contract contract = contractRepository.findById(dto.getId()).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACT_NOT_FOUND)
        );

        String guid = fileNetService.uploadFileToFilenet(contract.getProject(), file, username);
        contract.setContractGUID(guid);
        contractRepository.save(contract);
    }

    @Override
    public DownloadFileDTO downloadFile(String guid) {

        return fileNetService.fetchByGuid(guid);
    }

    @Override
    @Transactional
    public void deleteFile(Long contractId, String guid, MhteUserPrincipal userPrincipal) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACT_NOT_FOUND)
        );

        // @TODO - PLACEHOLDER: CHANGE WITH PRINCIPAL USERNAME WHEN OKAY
        contract.setLastModifiedBy("MAJESTIX");
//        contract.setLastModifiedBy(userPrincipal.getUsername());
        contract.setContractGUID(null);
        contractRepository.save(contract);
    }


    private boolean negativeNumberValidator(Double n) {
        if (n > 0) {
            return true;
        }
        return false;
    }

    private ContractResponseDTO toContractResponseDTO(Contract contract) {
        ContractResponseDTO dto = new ContractResponseDTO();

        dto.setId(contract.getId());
        dto.setContractType(contract.getContractType());
        dto.setContractValue(contract.getContractValue());
        dto.setSigningDate(contract.getSigningDate());

        return dto;
    }

}