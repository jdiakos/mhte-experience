package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileNetService {

    // upload file to filenet
    String uploadFileToFilenet(Project project, MultipartFile file, String username);

    // fetch file from filenet by guid
    DownloadFileDTO fetchByGuid(String guid);

}
