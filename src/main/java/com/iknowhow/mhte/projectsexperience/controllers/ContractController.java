package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import com.iknowhow.mhte.projectsexperience.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {

    Logger logger = LoggerFactory.getLogger(ContractController.class);

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) { this.contractService = contractService; }

    // @TODO - REMOVE ON REFACTOR
    @PostMapping(value= "/create-contract", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ContractDTO> createNewContract(@RequestBody ContractDTO contract) {
        logger.info("Create new contract");
        ContractDTO response = contractService.createNewContract(contract);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping(value= "/save-file")
    public ResponseEntity<String> uploadFile(@RequestPart("contract") ContractDTO contract,
                                             @RequestPart("file") MultipartFile document, @AuthenticationPrincipal MhteUserPrincipal userPrincipal) {
        //@TODO - CHANGE WITH PRINCIPAL USERNAME WHEN OKAY
        contractService.uploadFile(contract, document, "test_user");
        //contractService.uploadFile(contract, document, userPrincipal.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PutMapping(value="/update-contract")
    public ResponseEntity<ContractDTO> updateContract(@RequestBody ContractDTO contract){
        logger.info("Update contract with id: " + contract.getId());
        ContractDTO response = contractService.updateContract(contract);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value="/delete-contract/{id}")
    public ResponseEntity<ContractDTO> deleteContract(@PathVariable(value="id") Long id){
        logger.info("Request to delete contract with id: " + id);
        ContractDTO response = contractService.deleteContract(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value="/{projectId}/get-all")
    public ResponseEntity<List<ContractResponseDTO>> getAllContractsByProject(@PathVariable(value="projectId") Long projectId){
        logger.info("Fetching all contracts of project with id: " + projectId);
        List<ContractResponseDTO> response = contractService.getAllContractsByProject(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value="/all")
    public ResponseEntity<Page<ContractDTO>> fetchAllContractsPaginated(Pageable pageable) {
        logger.info("Fetch all contract paginated");
        Page<ContractDTO> response = contractService.fetchAllContractsPaginated(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/download-file/{guid}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("guid") String guid) {
        logger.info("Downloading file");
        DownloadFileDTO dto = contractService.downloadFile(guid);
        logger.info(dto.getFilename());

        ContentDisposition contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(dto.getFilename())
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dto.getFile());
    }

    @DeleteMapping("/delete-file/{contractId}/{guid}")
    public ResponseEntity<Void> deleteFile(@PathVariable("contractId") Long contractId,
                                           @PathVariable("guid") String guid,
                                           @AuthenticationPrincipal MhteUserPrincipal userPrincipal) {
        logger.info("Deleting file");
        contractService.deleteFile(contractId, guid, userPrincipal);

        return ResponseEntity.ok().build();
    }

}
