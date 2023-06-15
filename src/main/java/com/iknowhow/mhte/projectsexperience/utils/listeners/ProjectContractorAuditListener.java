package com.iknowhow.mhte.projectsexperience.utils.listeners;

import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class ProjectContractorAuditListener {

    @PrePersist
    public void setCreatedOn(ProjectContractor projectContractor) {
        if (projectContractor != null) {
            projectContractor.setLastModificationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void setUpdatedOn(ProjectContractor projectContractor) {
        if (projectContractor != null) {
            projectContractor.setLastModificationDate(LocalDateTime.now());
        }
    }
}
