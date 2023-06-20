package com.iknowhow.mhte.projectsexperience.utils.listeners;

import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectDocument;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class ProjectDocumentAuditListener {


    @PrePersist
    public void setCreatedOn(ProjectDocument projectDocument) {
        if (projectDocument != null) {
            projectDocument.setLastModificationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void setUpdatedOn(ProjectDocument projectDocument) {
        if (projectDocument != null) {
            projectDocument.setLastModificationDate(LocalDateTime.now());
        }
    }

}
