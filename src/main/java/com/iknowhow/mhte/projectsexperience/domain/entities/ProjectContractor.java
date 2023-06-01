package com.iknowhow.mhte.projectsexperience.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "project_contractor")
@Getter
@Setter
public class ProjectContractor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "project_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Project project;

    @Column(name = "contractor_id")
    private Long contractorId;

    @Column(name = "participation_type")
    private String participationType;

    @Column(name = "participation_percentage")
    private Double participationPercentage;

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
