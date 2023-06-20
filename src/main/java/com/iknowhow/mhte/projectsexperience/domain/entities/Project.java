package com.iknowhow.mhte.projectsexperience.domain.entities;

import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import com.iknowhow.mhte.projectsexperience.utils.listeners.ProjectAuditListener;
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
import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
@Audited
@EntityListeners(ProjectAuditListener.class)
public class Project implements Serializable {

    //@TODO - PROBABLY WE HAVE TO CHANGE WHICH FIELDS ARE GOING TO BE AUDITED
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_category")
    @Enumerated(EnumType.STRING)
    @NotAudited
    private ProjectsCategoryEnum projectCategory;

    @Column(name = "adam", unique = true)
    @NotAudited
    private String adam;

    @Column(name = "protocol_number", unique = true)
    @NotAudited
    private String protocolNumber;

    @Column(name = "title")
    @NotAudited
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "responsible_entity")
    @NotAudited
    private String responsibleEntity;

    @Column(name = "contracting_authority")
    @NotAudited
    private String contractingAuthority;

    @Column(name = "head_authority")
    @NotAudited
    private String headAuthority;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @NotAudited
    private List<ProjectContractor> projectContractors;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @NotAudited
    private List<ProjectSubcontractor> projectSubcontractors;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @NotAudited
    private List<Contract> contracts;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @NotAudited
    private List<Comment> comments;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @NotAudited
    private List<ProjectDocument> projectDocuments;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    @NotAudited
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;


    // Financials

    @Column(name = "initial_contract_budget")
    @NotAudited
    private Double initialContractBudget;

    @Column(name = "initial_contract_value")
    @NotAudited
    private Double initialContractValue;

    @Column(name = "supplementary_value")
    @NotAudited
    private Double supplementaryContractValue;

    @Column(name = "ape_value")
    @NotAudited
    private Double apeValue;

    @Column(name = "total_value")
    @NotAudited
    private Double totalValue;

    @Column(name = "date_of_signing")
    @Temporal(TemporalType.DATE)
    @NotAudited
    private LocalDate dateOfSigning;

    @Column(name = "estimated_completion_date")
    @Temporal(TemporalType.DATE)
    @NotAudited
    private LocalDate estimatedCompletionDate;

    @Column(name = "completion_date")
    @Temporal(TemporalType.DATE)
    @NotAudited
    private LocalDate completionDate;

    @Column(name = "receipt_protocol_date")
    @Temporal(TemporalType.DATE)
    @NotAudited
    private LocalDate receiptProtocolDate;

    @Column(name = "receipt_protocol_number")
    @NotAudited
    private String receiptProtocolNumber;

    @Column(name = "\"type\"")
    @NotAudited
    private String type;
}
