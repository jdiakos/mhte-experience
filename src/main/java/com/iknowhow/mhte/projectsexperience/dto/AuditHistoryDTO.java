package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditHistoryDTO {

    private Integer revisionNumber;
    private String lastModifiedBy;
    private LocalDateTime LastModificationDate;

}
