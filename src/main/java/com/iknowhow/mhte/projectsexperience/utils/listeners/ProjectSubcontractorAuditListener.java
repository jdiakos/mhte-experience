package com.iknowhow.mhte.projectsexperience.utils.listeners;

import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class ProjectSubcontractorAuditListener {

    @PrePersist
    public void setCreatedOn(ProjectSubcontractor projectSubcontractor) {
        if (projectSubcontractor != null) {
            projectSubcontractor.setLastModificationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void setUpdatedOn(ProjectSubcontractor projectSubcontractor) {
        if (projectSubcontractor != null) {
            projectSubcontractor.setLastModificationDate(LocalDateTime.now());
        }
    }
}
