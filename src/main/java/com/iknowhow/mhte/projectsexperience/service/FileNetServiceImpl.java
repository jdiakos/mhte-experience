package com.iknowhow.mhte.projectsexperience.service;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.iknowhow.mhte.projectsexperience.configuration.FilenetConfig;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectFileNetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileNetServiceImpl implements FileNetService {

    Logger logger = LoggerFactory.getLogger(FileNetServiceImpl.class);

//    @Value("${app.filenet.folder.root}")
    private String rootFolder = "/mhte";

    private final FilenetConfig filenetConfig;

    @Autowired
    public FileNetServiceImpl(FilenetConfig filenetConfig) {
        this.filenetConfig = filenetConfig;
    }

    @Override
    public String uploadFileToFilenet(Project project, MultipartFile file, String username) {
        ObjectStore objectStore = filenetConfig.getObjectStore();

        String name = "MHTEDoc";

        try {
            Document document = Factory.Document.createInstance(objectStore, name);
            document.getProperties().putValue("DocumentTitle", file.getOriginalFilename());
            document.getProperties().putValue("ProtocolNumber", project.getProtocolNumber());
            document.getProperties().putValue("ADAM", project.getAdam());
            document.getProperties().putValue("MHTEUser", username);

            ContentElementList ctList = Factory.ContentElement.createList();
            ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
            contentTransfer.setCaptureSource(file.getInputStream());
            contentTransfer.set_ContentType(file.getContentType());
            ctList.add(contentTransfer);

            document.set_ContentElements(ctList);

            document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
            document.save(RefreshMode.REFRESH);

            // ASSIGN TO FOLDER - IF FOLDER DOES NOT EXIST, CREATE BASED ON PROJECT PROTOCOL NUMBER
            Folder folder = fetchFolder(objectStore, project.getFilenetFolderName());
            ReferentialContainmentRelationship rcr = folder.file(
                    document, AutoUniqueName.AUTO_UNIQUE, "TEST", DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE
            );
            rcr.save(RefreshMode.REFRESH);

            return document.get_Id().toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        throw new MhteProjectFileNetException(MhteProjectErrorMessage.FILE_CANNOT_BE_UPLOADED.name());
    }

    @Override
    public DownloadFileDTO fetchByGuid(String guid) {
        DownloadFileDTO dto = new DownloadFileDTO();

        ObjectStore objectStore = filenetConfig.getObjectStore();
        Document document = Factory.Document.fetchInstance(objectStore, guid, null);
        ContentElementList ctl = document.get_ContentElements();

        for (Object obj : ctl) {
            ContentTransfer ct = (ContentTransfer) obj;
            try {
                dto.setFile(ct.accessContentStream().readAllBytes());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        dto.setFilename(document.get_Name());

        return dto;
    }


    private Folder fetchFolder(ObjectStore objectStore, String folderName) {
        Folder folder;
        try {

            return Factory.Folder.fetchInstance(objectStore, rootFolder + "/" + folderName, null);

        } catch (EngineRuntimeException e) {

            // IF SUBFOLDER DOES NOT EXIST, CREATE IT
            if (e.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND) {
                folder = Factory.Folder.createInstance(objectStore, null);
                folder.set_FolderName(folderName);
                folder.set_Parent(Factory.Folder.fetchInstance(objectStore, rootFolder, null));
                folder.save(RefreshMode.NO_REFRESH);
                return folder;
            }
            else {
                logger.error(e.getMessage());
            }
        }

        throw new MhteProjectFileNetException(MhteProjectErrorMessage.FILENET_FOLDER_ERROR.name());
    }

}
