package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.entities.ProjectDocument;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectDocumentRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectDocumentsDTO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectDocumentServiceImpl implements ProjectDocumentService {

    private final FileNetService fileNetService;
    private final ProjectDocumentRepository documentRepository;
    private final EntityManager entityManager;

    @Autowired
    public ProjectDocumentServiceImpl(FileNetService fileNetService,
                                      ProjectDocumentRepository documentRepository,
                                      EntityManager entityManager) {
        this.fileNetService = fileNetService;
        this.documentRepository = documentRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<ProjectDocument> assignDocumentsToProject(List<ProjectDocumentsDTO> dtoList, MultipartFile[] documents,
                                                          Project project,
                                                          MhteUserPrincipal userPrincipal) {

        List<ProjectDocument> projectDocuments = new ArrayList<>();
        // @TODO: Check why this fucking nonsense does not work (complains about detached entity)
//        if(dtoList!=null && dtoList.size()!=0) {
//            List<Long> list = dtoList.stream().map(d -> d.getId()).toList();
//            list.forEach(item -> {
//                ProjectDocument doc = documentRepository.findById(item).orElse(null);
//                if(doc != null){
//                    entityManager.merge(doc);
//                }
//                projectDocuments.add(doc);
//            });
//        	//projectDocuments.addAll(documentRepository.findAllByIdIn(list));
//        }

        if(dtoList!=null && dtoList.size()!=0) {
            dtoList.stream()
                    .map(dto -> documentRepository.findById(dto.getId()).orElse(null))
                    .filter(projectDocument -> projectDocument != null)
                    .forEach(doc -> {
                        entityManager.merge(doc);
                        projectDocuments.add(doc);
                    });
        }
        
        if(documents!=null && documents.length>0) {
	        for (MultipartFile multipartFile : documents) {
	            ProjectDocument document = new ProjectDocument();
	            document.setDateCreated(LocalDateTime.now());
	            document.setLastModifiedBy(userPrincipal.getUsername());
	            document.setGuid(fileNetService.uploadFileToFilenet(project, multipartFile, userPrincipal.getUsername()));
	            document.setFilename(multipartFile.getOriginalFilename());
	            document.setProject(project);
	            projectDocuments.add(document);
	        }
        }
        if(project.getId()!=null) {
        	project.getProjectDocuments().clear();
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
