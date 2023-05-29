package com.iknowhow.mhte.projectsexperience.domain.entities;

import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_category")
    @Enumerated(EnumType.STRING)
    private ProjectsCategoryEnum projectCategoy;

    @Column(name = "adam", unique = true)
    private String adam;

    @Column(name = "protocol_number", unique = true)
    private String protocolNumber;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "responsible_entity")
    private String responsibleEntity;

    @Column(name = "contracting_authority")
    private String contractingAuthority;

    @Column(name = "head_authority")
    private String headAuthority;

    @OneToMany(mappedBy = "project")
    private List<ProjectContractor> projectContractors;

    @OneToMany(mappedBy = "project")
    private List<ProjectSubcontractor> projectSubcontractors;

    @OneToMany(mappedBy = "project")
    private List<Contract> contracts;

    // Financials

    @Column(name = "initial_contract_budget")
    private Double initialContractBudget;

    @Column(name = "initial_contract_value")
    private Double initialContractValue;

    @Column(name = "supplementary_value")
    private Double supplementaryContractValue;

    @Column(name = "ape_value")
    private Double apeValue;

    @Column(name = "total_value")
    private Double totalValue;

    @Column(name = "date_of_signing")
    private LocalDate dateOfSigning;

    @Column(name = "estimated_completion_date")
    private LocalDate estimatedCompletionDate;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "receipt_protocol_date")
    private LocalDate receiptProtocolDate;

    @Column(name = "receipt_protocol_number")
    private String receiptProtocolNumber;

    @Column(name = "type")
    private String type;
}
