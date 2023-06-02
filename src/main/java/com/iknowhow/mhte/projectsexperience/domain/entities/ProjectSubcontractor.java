package com.iknowhow.mhte.projectsexperience.domain.entities;

import com.iknowhow.mhte.projectsexperience.utils.listeners.SubcontractorAuditListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "project_subcontractor")
@Getter
@Setter
@EntityListeners(SubcontractorAuditListener.class)
public class ProjectSubcontractor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "project_id", nullable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Project project;

    @Column(name = "subcontractor_id", nullable = false, updatable = false)
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
    private String contractGUID;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

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
