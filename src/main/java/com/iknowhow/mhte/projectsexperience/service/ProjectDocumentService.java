package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectDocumentService {

    List<ProjectDocument> assignDocumentsToProject(MultipartFile[] documents, Project project, MhteUserPrincipal userPrincipal);

}
