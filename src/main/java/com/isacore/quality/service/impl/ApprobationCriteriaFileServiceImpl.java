package com.isacore.quality.service.impl;

import java.io.File;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.model.InformationAditionalFile;
import com.isacore.quality.repository.IApprobationCriteriaFileRepo;
import com.isacore.quality.service.IApprobationCriteriaFileService;

@Service
public class ApprobationCriteriaFileServiceImpl implements IApprobationCriteriaFileService {

	private static final Log LOG = LogFactory.getLog(ApprobationCriteriaFileServiceImpl.class);
	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	@Autowired
	public IApprobationCriteriaFileRepo criteriaFileRepo;

	@Override
	public void deleteById(long criteriaFileId) {
		Optional<InformationAditionalFile> optional = criteriaFileRepo.findById(criteriaFileId);
		if (optional.isPresent()) {

			deleteFileLocal(optional.get().getPath());

			criteriaFileRepo.deleteById(criteriaFileId);

			LOG.info("Archivo eliminado del criterio.");
		}

	}

	private void deleteFileLocal(String path) {
		File file = new File(path);
		if (file.delete())
			LOG.info(String.format("Criterios Aprobación Archivo eliminado %s", path));
		else
			LOG.info(String.format("Criterios Aprobación Archivo no pudo ser eliminado %s", path));

	}

}
