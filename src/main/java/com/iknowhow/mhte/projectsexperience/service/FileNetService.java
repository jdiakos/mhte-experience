package com.iknowhow.mhte.projectsexperience.service;

import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileNetService {

    // upload file to filenet
    String uploadFileToFilenet(Contract contract, MultipartFile file, String username);

    // fetch file from filenet by guid
    DownloadFileDTO fetchByGuid(String guid);

    // delete file from filenet by guid
    void deleteDocumentByGuid(String guid);


}
