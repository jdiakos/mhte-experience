package com.iknowhow.mhte.projectsexperience.domain.entities;

import com.iknowhow.mhte.projectsexperience.domain.enums.ContractTypeEnum;
import com.iknowhow.mhte.projectsexperience.utils.listeners.ContractAuditListener;
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
@Table(name = "contract")
@Getter
@Setter
@Audited(withModifiedFlag = true)
@EntityListeners(ContractAuditListener.class)
public class Contract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "contract_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotAudited
    private ContractTypeEnum contractType;

    @Column(name = "contract_value", nullable = false)
    @NotAudited
    private Double contractValue;

    @Column(name = "signing_date", updatable = false, nullable = false)
    @NotAudited
    private LocalDate signingDate;

    @Column(name = "contract_guid")
    private String contractGUID;

    @Column(name = "filename")
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

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
        Contract contract = (Contract) o;
        return Objects.equals(id, contract.id) && contractType == contract.contractType && Objects.equals(contractValue, contract.contractValue) && Objects.equals(signingDate, contract.signingDate) && Objects.equals(contractGUID, contract.contractGUID) && Objects.equals(project, contract.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contractType, contractValue, signingDate, contractGUID, project);
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", contractType=" + contractType +
                ", contractValue=" + contractValue +
                ", signingDate=" + signingDate +
                ", contractGUID='" + contractGUID + '\'' +
                ", filename='" + filename + '\'' +
                ", dateCreated=" + dateCreated +
                ", lastModificationDate=" + lastModificationDate +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                '}';
    }
}
