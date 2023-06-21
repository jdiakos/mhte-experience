package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Comment;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.dto.CommentsDTO;

import java.util.List;

public interface CommentService {

    List<Comment> createComments(List<CommentsDTO> dtoList, Project project, MhteUserPrincipal userPrincipal);

    List<CommentsDTO> getAllCommentsOfProject(Project project);
}
