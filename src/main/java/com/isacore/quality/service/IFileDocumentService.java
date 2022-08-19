package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.FileDocument;
import com.isacore.util.CRUD;

public interface IFileDocumentService extends CRUD<FileDocument> {

	public void download(FileDocument[] lf);
}
