package com.isacore.quality.service;

import com.isacore.quality.model.QualityCertificate;
import com.isacore.util.CRUD;

public interface IQualityCertificateService extends CRUD<QualityCertificate>{

	Integer findCertificateByPK(long idHcc, Integer idC);

	void createCertificate(QualityCertificate qc);
	
	void updateCount(Integer countC, long idHcc, Integer idC, String clientPrint);
	
}
