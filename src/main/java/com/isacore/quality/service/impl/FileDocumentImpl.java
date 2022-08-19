package com.isacore.quality.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.FileDocument;
import com.isacore.quality.repository.IFileDocumentRepo;
import com.isacore.quality.service.IFileDocumentService;
import com.isacore.util.PassFileToRepository;

@Service
public class FileDocumentImpl implements IFileDocumentService {
	
	@Autowired
	private IFileDocumentRepo repo;
	
	@Override
	public List<FileDocument> findAll() {
		List<FileDocument> listFD = this.repo.findAll();
		return listFD;
	}

	@Override
	public FileDocument create(FileDocument obj) {
		return this.repo.save(obj);
	}

	@Override
	public FileDocument findById(FileDocument fDd) {
		Optional<FileDocument> fD = this.repo.findById(fDd.getIdFile());
		return fD.get();
	}

	@Override
	public FileDocument update(FileDocument obj) {
		return this.repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		return true;
		
	}

	@Override
	public void download(FileDocument[] lf) {
		for(FileDocument fd: lf) {
			String b64= PassFileToRepository.fileToBase64(fd.getUrl(), fd.getExtension());
			fd.setBase64File(b64);
		}
	}

}
