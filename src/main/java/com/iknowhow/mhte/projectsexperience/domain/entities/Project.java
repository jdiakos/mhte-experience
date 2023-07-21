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
import java.util.Objects;

@Entity
@Table(name = "project")
@Getter
@Setter
@Audited(withModifiedFlag = true)
@EntityListeners(ProjectAuditListener.class)
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_category")
    @Enumerated(EnumType.STRING)
    private ProjectsCategoryEnum projectCategory;

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

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectContractor> projectContractors;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectSubcontractor> projectSubcontractors;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotAudited
    private List<Comment> comments;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectDocument> projectDocuments;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences;

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
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfSigning;

    @Column(name = "estimated_completion_date")
    @Temporal(TemporalType.DATE)
    private LocalDate estimatedCompletionDate;

    @Column(name = "completion_date")
    @Temporal(TemporalType.DATE)
    private LocalDate completionDate;

    @Column(name = "receipt_protocol_date")
    @Temporal(TemporalType.DATE)
    private LocalDate receiptProtocolDate;

    @Column(name = "receipt_protocol_number")
    private String receiptProtocolNumber;

    @Column(name = "\"type\"")
    private String type;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project that = (Project) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
//    public void removeContractor(ProjectContractor contractor) {
//    	contractor.setProject(null);
//    	this.projectContractors.remove(contractor);
//    }
    
    public void addContractors(List<ProjectContractor> contractors) {
    	this.projectContractors.addAll(contractors);
    }
    
    public void addContracts(List<Contract> contracts) {
    	this.contracts.addAll(contracts);
    }
    
    public void addComments(List<Comment> comments) {
    	this.comments.addAll(comments);
    }
    
    public void addProjectDocuments(List<ProjectDocument> projectDocuments) {
    	this.projectDocuments.addAll(projectDocuments);
    }
    
    public void addSubcontractor(List<ProjectSubcontractor> subcontractors) {
    	this.projectSubcontractors.addAll(subcontractors);
    }
    
//    public void removeSubcontractor(ProjectSubcontractor subcontractor) {
//    	subcontractor.setProject(null);
//    	this.projectSubcontractors.remove(subcontractor);
//    }
//
//    public ProjectContractor getContractorById(Long id) {
//    	return this.projectContractors.stream().filter(p -> p.getId()==id).findFirst().orElse(new ProjectContractor());
//    }


}
