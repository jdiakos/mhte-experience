package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.domain.repository.ContractRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractServiceImpl implements ContractService{

    Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractRepository contractRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository){
        this.contractRepository = contractRepository;
    }

    @Override
    public ContractDTO createNewContract(ContractDTO contract) {
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
    public ContractDTO updateContract(ContractDTO contract) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Contract contractExists = contractRepository.findById(contract.getId()).orElse(null);
        if (contractExists == null) {
            throw new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACT_NOT_FOUND);
        }

        contractExists = modelMapper.map(contract, Contract.class);
        contractRepository.save(contractExists);
        return modelMapper.map(contractExists, ContractDTO.class);
    }

    @Override
    @Transactional
    public ContractDTO deleteContract(String id) {
        Contract contract = contractRepository.findById(Long.parseLong(id)).orElseThrow(() ->
                new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACT_NOT_FOUND));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ContractDTO contractDTO = modelMapper.map(contract, ContractDTO.class);
        contractRepository.delete(contract);

        return contractDTO;
    }
}
