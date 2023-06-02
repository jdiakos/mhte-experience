package com.iknowhow.mhte.projectsexperience.domain.entities;

import com.iknowhow.mhte.projectsexperience.utils.listeners.ContractorAuditListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "project_contractor")
@Getter
@Setter
@EntityListeners(ContractorAuditListener.class)
public class ProjectContractor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "project_id", nullable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Project project;

    @Column(name = "contractor_id", nullable = false, updatable = false)
    private Long contractorId;

    @Column(name = "participation_type", nullable = false)
    private String participationType;

    @Column(name = "participation_percentage", nullable = false)
    private Double participationPercentage;

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
        ProjectContractor that = (ProjectContractor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
