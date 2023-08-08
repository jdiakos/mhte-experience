package com.iknowhow.mhte.projectsexperience.domain.repository;

import com.iknowhow.mhte.projectsexperience.domain.entities.Experience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.iknowhow.mhte.projectsexperience.domain.enums.StudyCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    Page<Experience> findAllByCategoryAndPersonTaxId(StudyCategories category, String personTaxId, Pageable pageable);


	Page<Experience> findAllByIdIn(List<Long> experienceIds, Pageable page);
}
