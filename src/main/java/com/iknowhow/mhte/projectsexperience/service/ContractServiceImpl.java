package com.iknowhow.mhte.projectsexperience.service;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.iknowhow.mhte.projectsexperience.configuration.FilenetConfig;
import com.iknowhow.mhte.projectsexperience.domain.entities.Contract;
import com.iknowhow.mhte.projectsexperience.domain.entities.Project;
import com.iknowhow.mhte.projectsexperience.domain.repository.ContractRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractProjectDTO;
import com.iknowhow.mhte.projectsexperience.domain.repository.ProjectRepository;
import com.iknowhow.mhte.projectsexperience.dto.ContractResponseDTO;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectCustomValidationException;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectErrorMessage;
import com.iknowhow.mhte.projectsexperience.exception.MhteProjectsNotFoundException;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService{

    Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractRepository contractRepository;
    private final ProjectRepository projectRepository;
    private final FilenetConfig filenetConfig;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository,
                               ProjectRepository projectRepository,
                               FilenetConfig filenetConfig){
        this.contractRepository = contractRepository;
        this.projectRepository = projectRepository;
        this.filenetConfig = filenetConfig;
    }
    
    @Override
    public ContractProjectDTO createNewContract(ContractProjectDTO contract) {
    	if(!negativeNumberValidator(contract.getContractValue())) {
    		throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
    	}
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Contract newContract = modelMapper.map(contract, Contract.class);

        try{
            Contract savedContract = contractRepository.save(newContract);
            contract.setId(savedContract.getId());
        } catch (Exception ex) {
        	throw ex;
        }
        return contract;
    }

    @Override
    public ContractProjectDTO updateContract(ContractProjectDTO contract) {
    	if(!negativeNumberValidator(contract.getContractValue())) {
    		throw new MhteProjectCustomValidationException(MhteProjectErrorMessage.VALUES_CANNOT_BE_NEGATIVE);
    	}
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Contract contractExists = contractRepository.findById(contract.getId()).orElse(null);
        if (contractExists == null) {
            throw new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACT_NOT_FOUND);
        }
        Project current = (contractExists.getProject() == null) ? null : contractExists.getProject();
        contractExists = modelMapper.map(contract, Contract.class);
        contractExists.setProject(current);
        contractRepository.save(contractExists);
        return modelMapper.map(contractExists, ContractProjectDTO.class);
    }

    @Override
    @Transactional
    public ContractProjectDTO deleteContract(Long id) {
        Contract contract = contractRepository.findById(id).orElseThrow(() ->
                new MhteProjectsNotFoundException(MhteProjectErrorMessage.CONTRACT_NOT_FOUND));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ContractProjectDTO contractDTO = modelMapper.map(contract, ContractProjectDTO.class);
        contractRepository.delete(contract);

        return contractDTO;
    }

    @Override
    public List<ContractResponseDTO> getAllContractsByProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new MhteProjectsNotFoundException(MhteProjectErrorMessage.PROJECT_NOT_FOUND));

        return contractRepository.findAllByProjectId(projectId)
                .stream()
                .map(this::toContractResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ContractProjectDTO> fetchAllContractsPaginated(Pageable page){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return contractRepository.findAll(page).map(contract -> modelMapper.map(contract, ContractProjectDTO.class));
    }
    
    private boolean negativeNumberValidator(Double n) {
    	if(n>0) {
    		return true;
    	}
    	return false;
    }

    @Override
    public void uploadFile(ContractProjectDTO contract, MultipartFile document) {
//        fetchFolder("ΜΗΤΕ");
        try {
            uploadToFileNet(document);
//            fetchDocument(document.getOriginalFilename());
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private ContractResponseDTO toContractResponseDTO(Contract contract) {
        ContractResponseDTO dto = new ContractResponseDTO();

        dto.setId(contract.getId());
        dto.setContractType(contract.getContractType());
        dto.setContractValue(contract.getContractValue());
        dto.setSigningDate(contract.getSigningDate());

        return dto;
    }

    private void uploadToFileNet(MultipartFile file) throws IOException {
        try {
            ObjectStore os = filenetConfig.getObjectStore();

            String name = "DOCUMENT";
            Document document = Factory.Document.createInstance(os, name);
            document.getProperties().putValue("DocumentTitle", file.getOriginalFilename());

            ContentElementList contentElementList = Factory.ContentElement.createList();
            ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
            contentTransfer.setCaptureSource(file.getInputStream());
            contentTransfer.set_ContentType(file.getContentType());
            contentElementList.add(contentTransfer);

            document.set_ContentElements(contentElementList);

            document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
            document.save(RefreshMode.REFRESH);

            Folder folder = fetchFolder("ΜΗΤΕ");
            ReferentialContainmentRelationship rcr = folder.file(
                    document, AutoUniqueName.AUTO_UNIQUE, "TEST", DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE
            );
            rcr.save(RefreshMode.REFRESH);


        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

    }

    private void fetchDocument(String filename) {
        ObjectStore objectStore = filenetConfig.getObjectStore();
        String query = "SELECT * FROM Document WHERE DocumentTitle = '" + filename + "'";
        SearchSQL searchSQL = new SearchSQL(query);
        SearchScope searchScope = new SearchScope(objectStore);
//        IndependentObjectSet results = searchScope.fetchObjects(searchSQL, null, new PropertyFilter(), Boolean.FALSE);
        RepositoryRowSet rows = searchScope.fetchRows(searchSQL, null, new PropertyFilter(), Boolean.FALSE);
//        System.out.println(rows);
        for(Iterator<?> iterator = rows.iterator(); iterator.hasNext();) {
            RepositoryRow row = (RepositoryRow) iterator.next();
            System.out.println(row.getProperties());
        }

    }


    private Folder fetchFolder(String folderName) {
        try {
            Folder folder = Factory.Folder.fetchInstance(filenetConfig.getObjectStore(), "/" + folderName, null);
//            logger.info("NAME " + folder.get_FolderName());
            return folder;
        } catch (EngineRuntimeException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}
