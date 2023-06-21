package com.iknowhow.mhte.projectsexperience.controllers;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.dto.ContractDTO;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import com.iknowhow.mhte.projectsexperience.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    // @TODO - MOVE TO PROJECT CONTROLLER
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

    // @TODO - MOVE TO PROJECT CONTROLLER
    @DeleteMapping("/delete-file/{contractId}/{guid}")
    public ResponseEntity<Void> deleteFile(@PathVariable("contractId") Long contractId,
                                           @PathVariable("guid") String guid,
                                           @AuthenticationPrincipal MhteUserPrincipal userPrincipal) {
        logger.info("Deleting file");
        contractService.deleteFile(contractId, guid, userPrincipal);

        return ResponseEntity.ok().build();
    }

}
