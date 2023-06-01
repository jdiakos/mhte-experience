package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract")
public class ContractController {

    Logger logger = LoggerFactory.getLogger(ContractController.class);

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) { this.contractService = contractService; }

    @PostMapping(value= "/create-contract", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ContractDTO> createNewContract(@RequestBody ContractDTO contract) {
        logger.info("Create new contract");
        ContractDTO response = contractService.createNewContract(contract);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value="/update-contract")
    public ResponseEntity<ContractDTO> updateContract(@RequestBody ContractDTO contract){
        logger.info("Update contract with id: " + contract.getId());
        ContractDTO response = contractService.updateContract(contract);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value="/delete-contract/{id}")
    public ResponseEntity<ContractDTO> deleteContract(@PathVariable(value="id") String id){
        logger.info("Request to delete contract with id: " + id);
        ContractDTO response = contractService.deleteContract(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
