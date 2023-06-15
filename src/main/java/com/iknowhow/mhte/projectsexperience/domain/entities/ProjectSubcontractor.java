package com.iknowhow.mhte.projectsexperience.domain.entities;

import com.iknowhow.mhte.projectsexperience.utils.listeners.ProjectSubcontractorAuditListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "project_subcontractor")
@Getter
@Setter
@Audited
@EntityListeners(ProjectSubcontractorAuditListener.class)
public class ProjectSubcontractor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "project_id", nullable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotAudited
    private Project project;

    @Column(name = "subcontractor_id", nullable = false, updatable = false)
    @NotAudited
    private Long subcontractorId;

    @Column(name = "participation_type", nullable = false)
    private String participationType;

    @Column(name = "contract_value", nullable = false)
    private Double contractValue;

    @Column(name = "contract_date_from", nullable = false)
    private LocalDate contractDateFrom;

    @Column(name = "contract_date_to", nullable = false)
    private LocalDate contractDateTo;

    @Column(name = "contract_guid", nullable = false, updatable = false)
    @NotAudited
    private String contractGUID;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    @NotAudited
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectSubcontractor that = (ProjectSubcontractor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
