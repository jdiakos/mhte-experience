package com.iknowhow.mhte.projectsexperience.dto;

import com.iknowhow.mhte.projectsexperience.domain.enums.ContractTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractResponseDTO {

    @NotNull
    private Long id;
    @NotBlank
    private ContractTypeEnum contractType;
    @NotNull
    private Double contractValue;
    @NotNull
    private LocalDate signingDate;
}
