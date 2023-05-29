package com.iknowhow.mhte.projectsexperience.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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

}
