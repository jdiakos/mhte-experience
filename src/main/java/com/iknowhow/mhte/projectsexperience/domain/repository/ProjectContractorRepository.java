package com.iknowhow.mhte.projectsexperience.domain.repository;

import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProjectContractorRepository extends JpaRepository<ProjectContractor, Long> {

    List<ProjectContractor> findAllByProjectId(Long projectId);

}
