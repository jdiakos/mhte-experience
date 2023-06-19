package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileNetService {

    String uploadFileToFilenet(Project project, MultipartFile file, String username);

    DownloadFileDTO fetchByGuid(String guid);

}
