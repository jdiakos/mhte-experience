package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectDocument;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectDocumentServiceImpl implements ProjectDocumentService {

    private final ProjectDocumentRepository documentRepository;
    private final FileNetService fileNetService;

    @Autowired
    public ProjectDocumentServiceImpl(ProjectDocumentRepository documentRepository,
                                      FileNetService fileNetService) {
        this.documentRepository = documentRepository;
        this.fileNetService = fileNetService;
    }

    @Override
    public List<ProjectDocument> createDocuments(MultipartFile[] documents,
                                                 Project project,
                                                 MhteUserPrincipal userPrincipal) {

        List<ProjectDocument> projectDocuments = new ArrayList<>();

        for (MultipartFile multipartFile : documents) {
            ProjectDocument document = new ProjectDocument();
            document.setDateCreated(LocalDateTime.now());

            // @TODO - PLACEHOLDER, CHANGE WHEN USER PRINCIPAL AVAILABLE
            document.setLastModifiedBy("ASTERIX");
//            document.setLastModifiedBy(userPrincipal.getUsername());
            document.setGuid(fileNetService.uploadFileToFilenet(project, multipartFile, "ASTERIX"));
            document.setFilename(multipartFile.getOriginalFilename());

            projectDocuments.add(document);
        }

        return projectDocuments;
    }
}
