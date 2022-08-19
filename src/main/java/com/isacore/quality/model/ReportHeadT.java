package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REPORTHEADT")
public class ReportHeadT {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REPORT_ID")
	private Integer idReport;
	
	@Column(name = "REPORT_TYPE", nullable = true, length = 3072)
	private String type;
	
	@Column(name = "REPORT_TITLE", nullable = true, length = 3072)
	private String tittle;
	
	@Column(name = "REPORT_SUBTITLE", nullable = true, length = 3072)
	private String subTitle;
	
	@Column(name = "REPORT_CODE", nullable = true, length = 3072)
	private String code;
	
	@Column(name = "REPORT_CAMP1", nullable = true, length = 3072)
	private String camp1;
	
	@Column(name = "REPORT_CAMP2", nullable = true, length = 3072)
	private String camp2;
	
	@Column(name = "REPORT_CAMP3", nullable = true, length = 2048)
	private String camp3;
	
	@Column(name = "REPORT_CAMP4", nullable = true, length = 2048)
	private String camp4;
	

	public Integer getIdReport() {
		return idReport;
	}

	public void setIdReport(Integer idReport) {
		this.idReport = idReport;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCamp1() {
		return camp1;
	}

	public void setCamp1(String camp1) {
		this.camp1 = camp1;
	}

	public String getCamp2() {
		return camp2;
	}

	public void setCamp2(String camp2) {
		this.camp2 = camp2;
	}

	public String getCamp3() {
		return camp3;
	}

	public void setCamp3(String camp3) {
		this.camp3 = camp3;
	}

	public String getCamp4() {
		return camp4;
	}

	public void setCamp4(String camp4) {
		this.camp4 = camp4;
	}

	
}
