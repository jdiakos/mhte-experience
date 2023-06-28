package com.iknowhow.mhte.projectsexperience.dto;

import java.time.LocalDate;

import com.iknowhow.mhte.projectsexperience.domain.enums.ContractTypeEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContractDTO {
	
    private Long id;
    private ContractTypeEnum contractType;
    @NotNull
    private Double contractValue;
    @NotNull
    private LocalDate signingDate;
    private String contractGUID;
    private String filename;

}
