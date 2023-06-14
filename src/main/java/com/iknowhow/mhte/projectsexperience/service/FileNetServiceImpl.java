package com.iknowhow.mhte.projectsexperience.service;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.iknowhow.mhte.projectsexperience.configuration.FilenetConfig;
import com.iknowhow.mhte.projectsexperience.dto.DownloadFileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service
public class FileNetServiceImpl implements FileNetService {

    Logger logger = LoggerFactory.getLogger(FileNetServiceImpl.class);

    private final FilenetConfig filenetConfig;

    @Autowired
    public FileNetServiceImpl(FilenetConfig filenetConfig) {
        this.filenetConfig = filenetConfig;
    }

    @Override
    public String uploadFileToFilenet(String projectProtocolNo, MultipartFile file) {
        ObjectStore objectStore = filenetConfig.getObjectStore();
        // @TODO -- PLACEHOLDER
        String name = "DOCUMENT";

        try {
            Document document = Factory.Document.createInstance(objectStore, name);
            document.getProperties().putValue("DocumentTitle", file.getOriginalFilename());

            ContentElementList ctList = Factory.ContentElement.createList();
            ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
            contentTransfer.setCaptureSource(file.getInputStream());
            contentTransfer.set_ContentType(file.getContentType());
            ctList.add(contentTransfer);

            document.set_ContentElements(ctList);

            document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
            document.save(RefreshMode.REFRESH);

            // @TODO -- PLACEHOLDER
            Folder folder = fetchFolder("ΜΗΤΕ");
//            Folder folder = fetchFolder(projectProtocolNo);
            ReferentialContainmentRelationship rcr = folder.file(
                    document, AutoUniqueName.AUTO_UNIQUE, "TEST", DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE
            );
            rcr.save(RefreshMode.REFRESH);

            return document.get_Id().toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
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

    @Override
    public void deleteDocumentByGuid(String guid) {
        ObjectStore objectStore = filenetConfig.getObjectStore();
        Document document = Factory.Document.fetchInstance(objectStore, guid, null);
        document.delete();
        document.save(RefreshMode.REFRESH);
       // logger.info("DELETED DOCUMENT FROM FILENET WITH ID " + document.get_Id());
    }

    private Folder fetchFolder(String folderName) {
        Folder folder;
        try {
            return Factory.Folder.fetchInstance(filenetConfig.getObjectStore(), "/" + folderName, null);
        } catch (EngineRuntimeException e) {
            // @TODO: CREATE FOLDER IF NOT EXISTS
            if (e.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND) {
                folder = Factory.Folder.createInstance(filenetConfig.getObjectStore(), "/" + folderName, null);
                folder.getProperties().putValue("FolderName", folderName);
                folder.set_Parent(Factory.Folder.fetchInstance(filenetConfig.getObjectStore(), "/ΜΗΤΕ", null));
                folder.save(RefreshMode.REFRESH);
                return folder;
            }
            else {
                logger.error(e.getMessage());
            }
        }

        return null;
    }
}
