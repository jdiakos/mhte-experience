package com.iknowhow.mhte.projectsexperience.domain.entities;

import com.iknowhow.mhte.projectsexperience.domain.enums.ContractTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "contract")
@Getter
@Setter
public class Contract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "contract_type")
    @Enumerated(EnumType.STRING)
    private ContractTypeEnum contractType;

    @NotNull
    @Column(name = "contract_value")
    private Double contractValue;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "signing_date")
    private LocalDate signingDate;

    @Column(name = "contract_guid")
    private String contractGUID = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;


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
}
