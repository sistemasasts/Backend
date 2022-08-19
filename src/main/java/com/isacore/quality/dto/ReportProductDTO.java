package com.isacore.quality.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.isacore.quality.model.InformationAditional;
import com.isacore.quality.model.InformationAditionalType;

@SuppressWarnings("serial")
public class ReportProductDTO implements Serializable {

	private String name;

	private String nameGeneric;

	private String description;

	private String sapCode;

	private String itcdq;

	private String group;

	private String origin;

	private String specificUse;

	private String presentation;

	private String review;
	
	private String manipulationStorage;
	
	private String generalIndication;
	
	private String designation;
	
	private String referenceNorm;
	
	private String barcode;
	
	private String industrialSafetyRecommendation;
	
	private String inspectionSamplingTesting;
	
	private String armorType;

	private List<ReportProductSpecificationDTO> technical = new ArrayList<>();

	private List<ReportProductSpecificationDTO> view = new ArrayList<>();
	
	private List<InformationAditional> listCriteria = new ArrayList<>();
	
	private List<InformationAditional> listPaletizado = new ArrayList<>();

	public ReportProductDTO() {
	}

	public ReportProductDTO(String name, String nameGeneric, String description, String sapCode, String itcdq,
			String group, String origin, String specificUse, String presentation, String review, String manipulationStorage, String generalIndication,
			List<ReportProductSpecificationDTO> properties, List<InformationAditional> listInfoAditionals, String designation, 
			String referenceNorm, String barcode, String industrialSafetyRecommendation, String inspectionSamplingTesting, String armorType) {
		super();
		this.name = name;
		this.nameGeneric = nameGeneric;
		this.description = description;
		this.sapCode = sapCode;
		this.itcdq = itcdq;
		this.group = group;
		this.origin = origin;
		this.specificUse = specificUse;
		this.presentation = presentation;
		this.review = review;
		this.manipulationStorage = manipulationStorage;
		this.generalIndication = generalIndication;
		this.designation = designation;
		this.referenceNorm = referenceNorm;
		this.barcode = barcode;
		this.industrialSafetyRecommendation = industrialSafetyRecommendation;
		this.inspectionSamplingTesting = inspectionSamplingTesting;
		this.armorType = armorType;
		separate(properties);
		separateInfoAditional(listInfoAditionals);
	}

	private void separate(List<ReportProductSpecificationDTO> properties) {
		technical = properties.stream().filter(x -> x.getType().equalsIgnoreCase("T")).collect(Collectors.toList());
		view = properties.stream().filter(x -> x.getType().equalsIgnoreCase("V")).collect(Collectors.toList());
	}
	
	private void separateInfoAditional(List<InformationAditional> listInfo) {
		listCriteria = listInfo.stream().filter(x -> x.getType().equals(InformationAditionalType.CRITERIOS_APROBACION)).collect(Collectors.toList());
		listPaletizado = listInfo.stream().filter(x -> x.getType().equals(InformationAditionalType.CARATERISTICAS_PALETIZADO)).collect(Collectors.toList());
	}

	public String getName() {
		return name;
	}

	public String getNameGeneric() {
		return nameGeneric;
	}

	public String getDescription() {
		return description;
	}

	public String getSapCode() {
		return sapCode;
	}

	public String getItcdq() {
		return itcdq;
	}

	public String getGroup() {
		return group;
	}

	public String getOrigin() {
		return origin;
	}

	public String getSpecificUse() {
		return specificUse;
	}

	public String getPresentation() {
		return presentation;
	}

	public String getReview() {
		return review;
	}

	public String getDesignation() {
		return designation;
	}

	public String getReferenceNorm() {
		return referenceNorm;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getIndustrialSafetyRecommendation() {
		return industrialSafetyRecommendation != null
				? industrialSafetyRecommendation.replace("<strong>", "<b>").replace("</strong>", "</b>").replace("<em>", "<i>")
						.replace("</em>", "</i>")
				: industrialSafetyRecommendation;
	}

	public List<ReportProductSpecificationDTO> getTechnical() {
		return technical;
	}

	public List<ReportProductSpecificationDTO> getView() {
		return view;
	}

	public String getManipulationStorage() {
		return manipulationStorage != null
				? manipulationStorage.replace("<strong>", "<b>").replace("</strong>", "</b>").replace("<em>", "<i>")
						.replace("</em>", "</i>")
				: manipulationStorage;
	}

	public String getGeneralIndication() {
		return generalIndication != null
				? generalIndication.replace("<strong>", "<b>").replace("</strong>", "</b>").replace("<em>", "<i>")
						.replace("</em>", "</i>")
				: generalIndication;
	}

	public List<InformationAditional> getListCriteria() {
		listCriteria.forEach(x -> x.getDescription().replace("<strong>", "<b>").replace("</strong>", "</b>").replace("<em>", "<i>")
				.replace("</em>", "</i>"));
		return listCriteria;
	}
	
	public List<InformationAditional> getListPaletizado() {
		listPaletizado.forEach(x -> x.getDescription().replace("<strong>", "<b>").replace("</strong>", "</b>").replace("<em>", "<i>")
				.replace("</em>", "</i>"));
		return listPaletizado;
	}

	public boolean printListCriteria() {
		return !listCriteria.isEmpty();
	}

	public String getInspectionSamplingTesting() {
		return inspectionSamplingTesting  != null
				? inspectionSamplingTesting.replace("<strong>", "<b>").replace("</strong>", "</b>").replace("<em>", "<i>")
						.replace("</em>", "</i>")
				: inspectionSamplingTesting;
	}

	public String getArmorType() {
		return armorType;
	}

}
