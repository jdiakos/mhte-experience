package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Comment;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.dto.ProjectCommentsDTO;

import java.util.List;

public interface CommentService {

    List<Comment> createComments(List<ProjectCommentsDTO> dtoList, Project project, MhteUserPrincipal userPrincipal);
}
