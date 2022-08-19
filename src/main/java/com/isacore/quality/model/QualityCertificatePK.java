package com.isacore.quality.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class QualityCertificatePK implements Serializable{

	@ManyToOne(cascade = {CascadeType.PERSIST})
	@JoinColumn(name = "", nullable = false)
	private HccHead hccHead;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "", nullable = false)
	private ClientImptek clientImptek;

	public HccHead getHccHead() {
		return hccHead;
	}

	public void setHccHead(HccHead hccHead) {
		this.hccHead = hccHead;
	}

	public ClientImptek getClientImptek() {
		return clientImptek;
	}

	public void setClientImptek(ClientImptek clientImptek) {
		this.clientImptek = clientImptek;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientImptek == null) ? 0 : clientImptek.hashCode());
		result = prime * result + ((hccHead == null) ? 0 : hccHead.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QualityCertificatePK other = (QualityCertificatePK) obj;
		if (clientImptek == null) {
			if (other.clientImptek != null)
				return false;
		} else if (!clientImptek.equals(other.clientImptek))
			return false;
		if (hccHead == null) {
			if (other.hccHead != null)
				return false;
		} else if (!hccHead.equals(other.hccHead))
			return false;
		return true;
	}
	
	
	
}
