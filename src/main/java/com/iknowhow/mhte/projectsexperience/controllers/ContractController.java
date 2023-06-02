package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.dto.ContractProjectDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;
import com.iknowhow.mhte.projectsexperience.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {

    Logger logger = LoggerFactory.getLogger(ContractController.class);

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) { this.contractService = contractService; }

    @PostMapping(value= "/create-contract", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ContractProjectDTO> createNewContract(@RequestBody ContractProjectDTO contract) {
        logger.info("Create new contract");
        ContractProjectDTO response = contractService.createNewContract(contract);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value="/update-contract")
    public ResponseEntity<ContractProjectDTO> updateContract(@RequestBody ContractProjectDTO contract){
        logger.info("Update contract with id: " + contract.getId());
        ContractProjectDTO response = contractService.updateContract(contract);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value="/delete-contract/{id}")
    public ResponseEntity<ContractProjectDTO> deleteContract(@PathVariable(value="id") Long id){
        logger.info("Request to delete contract with id: " + id);
        ContractProjectDTO response = contractService.deleteContract(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value="/{projectId}/get-all")
    public ResponseEntity<List<ContractResponseDTO>> getAllContractsByProject(@PathVariable(value="projectId") Long projectId){
        logger.info("Fetching all contracts of project with id: " + projectId);
        List<ContractResponseDTO> response = contractService.getAllContractsByProject(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value="/all")
    public ResponseEntity<Page<ContractProjectDTO>> fetchAllContractsPaginated(Pageable pageable) {
        logger.info("Fetch all contract paginated");
        Page<ContractProjectDTO> response = contractService.fetchAllContractsPaginated(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
