package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectContractor;
import com.iknowhow.mhte.projectsexperience.dto.ProjectContractorDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.CompanyInfoResponseDTO;
import com.iknowhow.mhte.projectsexperience.dto.feign.SearchCompanyInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProjectContractorService {

    Page<CompanyInfoResponseDTO> searchProjectContractors(SearchCompanyInfoDTO dto,
                                                          Pageable pageable);

    List<ProjectContractorDTO> getAllContractorsForProject(Long projectId);

	List<ProjectContractor> assignContractorsToProject(List<ProjectContractorDTO> dtoList, Project project,
			MhteUserPrincipal userPrincipal);

}
