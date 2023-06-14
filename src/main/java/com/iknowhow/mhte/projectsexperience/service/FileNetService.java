package com.iknowhow.mhte.projectsexperience.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileNetService {

    // upload file to filenet
    String uploadFileToFilenet(MultipartFile file);

    // fetch file from filenet by guid
    void fetchByGuid(String guid);

    // delete file from filenet by guid
    void deleteDocumentByGuid(String guid);


}
