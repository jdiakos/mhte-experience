package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.ContractRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectCustomValidationException;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

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
    public ContractDTO createNewContract(ContractDTO dto) {
        if (!negativeNumberValidator(dto.getContractValue())) {
            throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
        }
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(
                () -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND)
        );

        Contract contract = new Contract();
        contract.setContractType(dto.getContractType());
        contract.setContractValue(dto.getContractValue());
        contract.setSigningDate(dto.getSigningDate());
        contract.setProject(project);

        contractRepository.save(contract);
        ContractDTO response = utils.initModelMapperStrict().map(contract, ContractDTO.class);
        response.setProjectId(dto.getProjectId());

        return response;
    }

    @Override
    public ContractDTO updateContract(ContractDTO contract) {
        if (!negativeNumberValidator(contract.getContractValue())) {
            throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Contract contractExists = contractRepository.findById(contract.getId()).orElse(null);
        if (contractExists == null) {
            throw new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACT_NOT_FOUND);
        }
        Project current = (contractExists.getProject() == null) ? null : contractExists.getProject();
        contractExists = modelMapper.map(contract, Contract.class);
        contractExists.setProject(current);
        contractRepository.save(contractExists);
        return modelMapper.map(contractExists, ContractDTO.class);
    }

    @Override
    @Transactional
    public ContractDTO deleteContract(Long id) {
        Contract contract = contractRepository.findById(id).orElseThrow(() ->
                new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACT_NOT_FOUND));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ContractDTO contractDTO = modelMapper.map(contract, ContractDTO.class);
        contractRepository.delete(contract);

        return contractDTO;
    }

    @Override
    public List<ContractResponseDTO> getAllContractsByProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

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

        String guid = fileNetService.uploadFileToFilenet(contract, file, username);
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