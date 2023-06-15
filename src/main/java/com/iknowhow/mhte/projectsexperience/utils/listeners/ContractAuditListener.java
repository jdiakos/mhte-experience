package com.iknowhow.mhte.projectsexperience.utils.listeners;

import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class ContractAuditListener {

    @PrePersist
    public void setCreatedOn(Contract contract) {
        if (contract != null) {
            contract.setLastModificationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void setUpdateOn(Contract contract) {
        if (contract != null) {
            contract.setLastModificationDate(LocalDateTime.now());
        }
    }


}
