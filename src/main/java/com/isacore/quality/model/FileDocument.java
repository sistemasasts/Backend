package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "file_document")
@Table(name = "FILE_DOCUMENT")
public class FileDocument {

	@Id
	@Column(name = "FILE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idFile;
	
	@Column(name = "FILE_NAME", nullable = true)
	private String name;
	
	@Column(name = "FILE_URI", nullable = true)
	private String url;
	
	@Column(name = "FILE_EXTENSION", nullable = true)
	private String extension;
	
	/*Variable que determina si es Ficha Técnica o MSDS*/
	@Column(name = "FILE_TYPE", nullable = true)
	private String type;

	@Transient
	private String base64File;

	public Integer getIdFile() {
		return idFile;
	}

	public void setIdFile(Integer idFile) {
		this.idFile = idFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBase64File() {
		return base64File;
	}

	public void setBase64File(String base64File) {
		this.base64File = base64File;
	}
	
	
}
