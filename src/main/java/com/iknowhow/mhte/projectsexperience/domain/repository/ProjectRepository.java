package com.iknowhow.mhte.projectsexperience.domain.repository;

import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.enums.ProjectsCategoryEnum;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, QuerydslPredicateExecutor<Project> {
	
	Optional<Project> findByAdam(String adam);
	
	Optional<Project> findByContracts_Id(Long contractId);
	
	Project findByProtocolNumber(String number);
	
	Page<Project> findByProjectCategory(ProjectsCategoryEnum projectCategory, Pageable page);
	
	Page<Project> findByResponsibleEntity(String entity, Pageable page);

}
