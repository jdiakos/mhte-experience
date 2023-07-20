package com.iknowhow.mhte.projectsexperience.domain.entities;


import com.iknowhow.mhte.projectsexperience.utils.listeners.ProjectDocumentAuditListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "project_documents")
@Getter
@Setter
@Audited
@EntityListeners(ProjectDocumentAuditListener.class)
public class ProjectDocument implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "guid")
    private String guid;

    @Column(name = "filename")
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    @NotAudited
    private LocalDateTime dateCreated;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectDocument that = (ProjectDocument) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
