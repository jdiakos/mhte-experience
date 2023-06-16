package com.iknowhow.mhte.projectsexperience.utils.listeners;

import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class ProjectAuditListener {

    @PrePersist
    public void setCreatedOn (Project project) {
        if (project != null) {
            project.setLastModificationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void setUpdateOn(Project project) {
        if (project != null) {
            project.setLastModificationDate(LocalDateTime.now());
        }
    }

}
