package com.iknowhow.mhte.projectsexperience.dto;

import java.time.LocalDate;

import com.iknowhow.mhte.projectsexperience.domain.enums.ContractTypeEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContractDTO {
	
    private Long id;
    @NotBlank
    private ContractTypeEnum contractType;
    @NotBlank
    private Double contractValue;
    private LocalDate signingDate;

}
