package com.iknowhow.mhte.projectsexperience.domain.repository;

import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectSubcontractor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProjectSubcontractorRepository extends JpaRepository<ProjectSubcontractor, Long> {

    Page<ProjectSubcontractor> findAllByProjectId(Long projectId, Pageable pageable);

    Optional<ProjectSubcontractor> findBySubcontractorIdAndProjectId(Long subcontractorId, Long projectId);
}
