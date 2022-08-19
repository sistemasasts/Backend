package com.isacore.quality.dto;

import com.isacore.quality.model.ClientImptek;
import com.isacore.quality.model.HccHead;
import com.isacore.quality.model.QualityCertificate;
import com.isacore.quality.model.ReportHeadT;

public class ReportDto {

	private HccHead hccHead;
	
	private ClientImptek client;
	
	private QualityCertificate qc;
	
	private ReportHeadT rh;
	
	private String normProductText;

	public HccHead getHccHead() {
		return hccHead;
	}

	public void setHccHead(HccHead hccHead) {
		this.hccHead = hccHead;
	}

	public ClientImptek getClient() {
		return client;
	}

	public void setClient(ClientImptek client) {
		this.client = client;
	}

	public QualityCertificate getQc() {
		return qc;
	}

	public void setQc(QualityCertificate qc) {
		this.qc = qc;
	}

	public ReportHeadT getRh() {
		return rh;
	}

	public void setRh(ReportHeadT rh) {
		this.rh = rh;
	}

	public String getNormProductText() {
		return normProductText;
	}

	public void setNormProductText(String normProductText) {
		this.normProductText = normProductText;
	}
	
	
	
	
	
}
