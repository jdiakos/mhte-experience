package com.iknowhow.mhte.projectsexperience.domain.entities;

import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceOccupation;
import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceRole;
import com.iknowhow.mhte.projectsexperience.domain.enums.StudyCategories;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "experience")
@Getter
@Setter
@Audited
public class Experience implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "experience_from")
    private LocalDate experienceFrom;

    @Column(name = "experience_to")
    private LocalDate experienceTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private StudyCategories category;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ExperienceRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "occupation")
    private ExperienceOccupation occupation;

    @Column(name = "value")
    private Double value;

    @Column(name = "approved_period")
    private Long approvedPeriod;

    @Column(name = "approved_value")
    private Double approvedValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @Column(name = "person_tax_id")
    private String personTaxId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
