package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectDocument;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectDocumentRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectDocumentsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectDocumentServiceImpl implements ProjectDocumentService {

    private final FileNetService fileNetService;
    private final ProjectDocumentRepository documentRepository;

    @Autowired
    public ProjectDocumentServiceImpl(FileNetService fileNetService,
                                      ProjectDocumentRepository documentRepository) {
        this.fileNetService = fileNetService;
        this.documentRepository = documentRepository;
    }

    @Override
    public List<ProjectDocument> assignDocumentsToProject(MultipartFile[] documents,
                                                          Project project,
                                                          MhteUserPrincipal userPrincipal) {

        List<ProjectDocument> projectDocuments = new ArrayList<>();

        for (MultipartFile multipartFile : documents) {
            ProjectDocument document = new ProjectDocument();
            document.setDateCreated(LocalDateTime.now());
            document.setLastModifiedBy("ASTERIX");
//            document.setLastModifiedBy(userPrincipal.getUsername());
            document.setGuid(fileNetService.uploadFileToFilenet(project, multipartFile, "ASTERIX"));
            document.setFilename(multipartFile.getOriginalFilename());
            document.setProject(project);
            projectDocuments.add(document);
        }

        return projectDocuments;
    }

    @Override
    public List<ProjectDocumentsDTO> getAllOfProject(Project project) {
        return documentRepository.findAllByProjectId(project.getId())
                .stream()
                .map(this::toProjectDocumentResponse)
                .toList();
    }


    private ProjectDocumentsDTO toProjectDocumentResponse(ProjectDocument projectDocument) {
        ProjectDocumentsDTO dto = new ProjectDocumentsDTO();
        dto.setId(projectDocument.getId());
        dto.setFilename(projectDocument.getFilename());
        dto.setGuid(projectDocument.getGuid());

        return dto;
    }
}
