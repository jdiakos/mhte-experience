package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Comment;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.CommentRepository;
import com.iknowhow.mhte.projectsexperience.dto.CommentsDTO;
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
    public List<Comment> assignCommentsToProject(List<CommentsDTO> dtoList,
                                                 Project project,
                                                 MhteUserPrincipal userPrincipal) {

        return dtoList.stream().
        		filter(dto->dto.getId()==null).map(dto -> {
		        	Comment comment = new Comment();
			        comment.setCreatedAt(LocalDateTime.now());
			    	comment.setId(dto.getId());
			    	comment.setCreatedAt(dto.getDate());
			        comment.setMessage(dto.getMessage());
			        comment.setProject(project);
			        comment.setRole(dto.getRole());
			        comment.setCreatedBy(userPrincipal.getUsername());
				    return comment;
				}).toList();
    }

    @Override
    public List<CommentsDTO> getAllCommentsOfProject(Project project) {
        return commentRepository.findCommentsByProjectId(project.getId())
                .stream()
                .map(this::toProjectCommentsDTO)
                .toList();
    }


    private CommentsDTO toProjectCommentsDTO(Comment comment) {
        CommentsDTO dto = new CommentsDTO();
        dto.setId(comment.getId());
        dto.setMessage(comment.getMessage());
        dto.setUsername(comment.getCreatedBy());
        dto.setDate(comment.getCreatedAt());
        dto.setRole(comment.getRole());

        return dto;
    }
}
