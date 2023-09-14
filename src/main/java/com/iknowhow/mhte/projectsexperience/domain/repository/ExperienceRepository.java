package com.iknowhow.mhte.projectsexperience.domain.repository;

import com.iknowhow.mhte.projectsexperience.domain.entities.Experience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.iknowhow.mhte.projectsexperience.domain.enums.ExperienceCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    Page<Experience> findAllByCategoryAndPersonTaxIdIn(ExperienceCategories category,
                                                       List<String> personTaxIds,
                                                       Pageable pageable);


	Page<Experience> findAllByIdIn(List<Long> experienceIds, Pageable page);

    List<Experience> findAllByCompanyTaxIdAndCategoryAndExperienceFromAfter(String companyTaxId,
                                                                            ExperienceCategories category,
                                                                            LocalDate experienceFrom);

}
