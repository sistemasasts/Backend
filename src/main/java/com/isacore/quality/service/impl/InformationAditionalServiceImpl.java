package com.isacore.quality.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.isacore.quality.dto.InformationAditionalFileDTO;
import com.isacore.quality.exception.ApprobationCriteriaErrorException;
import com.isacore.quality.exception.FileReadErrorException;
import com.isacore.quality.model.InformationAditional;
import com.isacore.quality.model.InformationAditionalFile;
import com.isacore.quality.repository.IInformationAditionalRepo;
import com.isacore.quality.service.IInformationAditionalService;
import com.isacore.util.PassFileToRepository;

@Service
public class InformationAditionalServiceImpl implements IInformationAditionalService {

	private static String BASE_PATH ="D:/ISA/FilesRepository/productos/";
	//private static String BASE_PATH ="C:/Users/dalpala/Documents/ISADATOS/";
	
	private static final Log LOG = LogFactory.getLog(InformationAditionalServiceImpl.class);
	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	
	@Autowired
	public IInformationAditionalRepo infoAditionalRepo;
	
	@Autowired
	private Gson gsonLog;
	
	@Override
	public List<InformationAditional> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InformationAditional create(InformationAditional obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InformationAditional findById(InformationAditional id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public InformationAditional update(InformationAditional obj) {
		Optional<InformationAditional> criteriaOptional = infoAditionalRepo.findById(obj.getId());
		
		if(criteriaOptional.isPresent()) {
			InformationAditional criteria = criteriaOptional.get();
			
			criteria.setDescription(obj.getDescription());
			
			LOG.info(String.format("Criterio de aprobación Modificadado %s", gsonLog.toJson(criteria)));
			
			return criteria;
		}
		return null;
	}

	@Override
	public boolean delete(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional
	public InformationAditional addImage(String jsonCriteria, byte[] file ) {
		
		try {
			InformationAditionalFileDTO dto = JSON_MAPPER.readValue(jsonCriteria, InformationAditionalFileDTO.class);
			if(dto != null) {
				
				Optional<InformationAditional> optional = infoAditionalRepo.findById(dto.getCriteriaId());
				if(optional.isPresent()) {
					InformationAditional criteria = optional.get();
					try {
						final String path = createPathFile(dto.getNameFile());
						PassFileToRepository.saveLocalFile(path, file);
						criteria.addFile(new InformationAditionalFile(dto.getNameFile(),null,path,dto.getDescription(),null));	
						LOG.info(String.format("Archivo %s guardado para el criterio %s ", dto.getNameFile(), gsonLog.toJson(criteria)));
						return criteria;
						
					} catch (IOException e) {
						e.printStackTrace();
						throw new ApprobationCriteriaErrorException();
					}
					
				}				
				
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new ApprobationCriteriaErrorException();
		}		
		
		return null;
	}
	
	private String createPathFile(String nameFile) {
		String path = BASE_PATH.concat(nameFile);

		if (PassFileToRepository.fileExists(path))
			path = BASE_PATH.concat(PassFileToRepository.generateDateAsId()).concat("_").concat(nameFile);

		LOG.info(String.format("Ruta creada %s para guardar archivo %s", path, nameFile));
		return path;
	}

	@Override
	public byte[] readFile(String path) {
		// TODO Auto-generated method stub
		try {
			return PassFileToRepository.readLocalFile(path);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileReadErrorException();
		}
	}

	@Override
	public List<InformationAditionalFileDTO> readFiles(long criteriaId) {

		List<InformationAditionalFileDTO> files = new ArrayList<>();
		Optional<InformationAditional> optional = infoAditionalRepo.findById(criteriaId);
		if (optional.isPresent()) {
			InformationAditional criteria = optional.get();
			criteria.getDetailFile().forEach(x -> {
				files.add(new InformationAditionalFileDTO(x.getId(), x.getDescription(), x.getNameGroupFile(),
						x.getName(), PassFileToRepository.fileToBase64(x.getPath(),
								x.getName().substring(x.getName().length() - 3))));
			});
			
		}
		return files;
	}

	@Override
	public void deleteByCriteriaId(long criteriaId) {
		infoAditionalRepo.deleteById(criteriaId);
		LOG.info("Criterio eliminado");
	}


}
