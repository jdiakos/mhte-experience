package com.iknowhow.mhte.projectsexperience.domain.repository;

import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectSubcontractorRepository extends JpaRepository<ProjectSubcontractor, Long> {
}
