package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.authsecurity.security.MhteUserPrincipal;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.dto.ProjectCommentsDTO;

public interface CommentService {

    void postComment(ProjectCommentsDTO dto, Project project, MhteUserPrincipal userPrincipal);
}
