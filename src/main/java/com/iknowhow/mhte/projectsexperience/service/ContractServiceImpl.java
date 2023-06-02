package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import com.iknowhow.mhte.projectsexperience.domain.repository.ContractRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractProjectDTO;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractorResponseDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService{

    Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractRepository contractRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository,
                               ProjectRepository projectRepository){
        this.contractRepository = contractRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ContractProjectDTO createNewContract(ContractProjectDTO contract) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Contract newContract = modelMapper.map(contract, Contract.class);

        try{
            Contract savedContract = contractRepository.save(newContract);
            contract.setId(savedContract.getId());
        } catch (Exception ex) {
            //throw new MhteProjectExceptionHandler(ex.getMessage())
        }

        return contract;
    }

    @Override
    public ContractProjectDTO updateContract(ContractProjectDTO contract) {
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
        return modelMapper.map(contractExists, ContractProjectDTO.class);
    }

    @Override
    @Transactional
    public ContractProjectDTO deleteContract(String id) {
        Contract contract = contractRepository.findById(Long.parseLong(id)).orElseThrow(() ->
                new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACT_NOT_FOUND));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ContractProjectDTO contractDTO = modelMapper.map(contract, ContractProjectDTO.class);
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


    private ContractResponseDTO toContractResponseDTO(Contract contract) {
        ContractResponseDTO dto = new ContractResponseDTO();

        dto.setId(contract.getId());
        dto.setContractType(contract.getContractType());
        dto.setContractValue(contract.getContractValue());
        dto.setSigningDate(contract.getSigningDate());

        return dto;
    }
}
