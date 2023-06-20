package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Comment;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.CommentRepository;
import com.iknowhow.mhte.projectsexperience.dto.ProjectCommentsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void postComment(ProjectCommentsDTO dto, Project project, MhteUserPrincipal userPrincipal) {
        Comment comment = new Comment();
        comment.setMessage(dto.getMessage());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setCreatedBy(userPrincipal.getUsername());
        comment.setProject(project);

        commentRepository.save(comment);
    }
}
