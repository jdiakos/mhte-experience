package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Comment;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.CommentRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectCommentsDTO;
import com.iknowhow.mhte.projectsexperience.dto.ProjectCommentsResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> assignCommentsToProject(List<ProjectCommentsDTO> dtoList,
                                        Project project,
                                        MhteUserPrincipal userPrincipal) {

        return dtoList
                .stream()
                .map(dto -> {
                    Comment comment = new Comment();
                    comment.setMessage(dto.getMessage());
                    comment.setProject(project);
                    comment.setCreatedAt(LocalDateTime.now());
                    comment.setCreatedBy("ASTERIX");
                    return comment;
                })
                .toList();
    }

    @Override
    public List<ProjectCommentsResponseDTO> getAllCommentsOfProject(Project project) {
        return commentRepository.findCommentsByProjectId(project.getId())
                .stream()
                .map(this::toProjectCommentsResponseDTO)
                .toList();
    }


    private ProjectCommentsResponseDTO toProjectCommentsResponseDTO(Comment comment) {
        ProjectCommentsResponseDTO dto = new ProjectCommentsResponseDTO();
        dto.setId(comment.getId());
        dto.setMessage(comment.getMessage());
        dto.setUsername(comment.getCreatedBy());
        dto.setDate(comment.getCreatedAt());
        dto.setRole(comment.getRole());

        return dto;
    }
}
