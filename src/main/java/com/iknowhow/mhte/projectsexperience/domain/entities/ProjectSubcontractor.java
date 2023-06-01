package com.iknowhow.mhte.projectsexperience.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "project_subcontractor")
@Getter
@Setter
public class ProjectSubcontractor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "project_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Project project;

    @Column(name = "subcontractor_id")
    private Long subcontractorId;

    @Column(name = "participation_type")
    private String participationType;

    @Column(name = "contract_value")
    private Double contractValue;

    @Column(name = "contract_date_from")
    private LocalDate contractDateFrom;

    @Column(name = "contract_date_to")
    private LocalDate contractDateTo;

    @Column(name = "contract_guid")
    private String contractGUID;

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
