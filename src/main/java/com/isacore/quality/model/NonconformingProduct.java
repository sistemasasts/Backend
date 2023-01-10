package com.isacore.quality.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.localdate.converter.LocalDateConverter;
import com.isacore.localdate.converter.LocalDateTimeConverter;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;

@Entity(name = "nonconforming")
@Table(name = "NONCONFORMING_PRODUCT")
public class NonconformingProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NCP_ID")
    private Integer idNCP;

    @Column(name = "REP_TITLE", nullable = true, length = 2048)
    private String repTitle;

    @Column(name = "REP_SUBTITLE", nullable = true, length = 2048)
    private String repSubtitle;

    @Column(name = "REP_REFERENCE", nullable = true, length = 2048)
    private String repReference;

    @Column(name = "REP_REVIEW", nullable = true, length = 64)
    private String repReview;

    @Column(name = "REP_REGISTER", nullable = true, length = 64)
    private String repRegister;

    @Column(name = "REP_CODE", nullable = true, length = 64)
    private String repCode;

    @Column(name = "NCP_DATE_UPDATE", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializeIsa.class)
    @JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
    private LocalDateTime dateUpdate;

    @Column(name = "NCP_DETECTION_DATE", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate dateDetection;

    @Column(name = "NCP_PRODUCTION_DATE", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate dateProduction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = true, updatable = false)
    private Product product;

    @Column(name = "NCP_BATCH", nullable = true, length = 64)
    private String batch;

    @Transient
    private Double daysAntiquities;

    @Column(name = "NCP_PRODUCTION_ORDER", nullable = true, length = 64)
    private String orderProduction;

    @Column(name = "NCP_HCC_FREE_USE", nullable = true, length = 64)
    private String hccFreeUse;

    @Column(name = "NCP_SOURCE", nullable = true, length = 256)
    private String source;

    @Column(name = "NCP_SOURCE_LINE", nullable = true, length = 256)
    private String sourceLine;

    @Column(name = "NCP_AFFECTED_LINE", nullable = true, length = 256)
    private String affectedLine;

    @Column(name = "NCP_AMOUNT_PRODUCED", nullable = false, columnDefinition = "decimal(10,2)")
    private Double amountProduced;

    @Column(name = "NCP_NONCONFORMING_AMOUNT", nullable = false, columnDefinition = "decimal(10,2)")
    private Double amountNonConforming;

    @Column(name = "NCP_UNIT", length = 16)
    private String unitNCP;

    @OneToMany(fetch = FetchType.EAGER)
    //@Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "NCP_ID", nullable = true)
    private List<ExitMaterialHistory> exitMaterialH;


    @Column(name = "NCP_DEFECT", nullable = false, columnDefinition = "varchar(max)")
    private String defect;

    @Column(name = "NCP_EXISTS_INVENTORY", nullable = true)
    private Boolean existsInventory;

    @Column(name = "NCP_EXISTING_MATERIAL", nullable = true, columnDefinition = "decimal(10,2)")
    private Double existingMaterial;

    @Column(name = "NCP_PERCENT_VALIDITY", nullable = true, columnDefinition = "decimal(5,2)")
    private Double validityPercent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AREA_ID", referencedColumnName = "AREA_ID", insertable = true, updatable = false)
    private Area area;

    @Column(name = "NCP_FIVEM", nullable = false, length = 2048)
    private String fiveM;

    @Column(name = "NCP_UNIT_COST", nullable = true, columnDefinition = "decimal(5,2)")
    private Double unitCost;

    @Column(name = "NCP_TOTAL_COST", nullable = true, columnDefinition = "decimal(10,2)")
    private Double totalCost;

    @Column(name = "NCP_PERCENT", nullable = true, columnDefinition = "decimal(19,2)")
    private Double percentPNC;

    @Column(name = "NCP_GOAL", nullable = true, columnDefinition = "decimal(5,2)")
    private Double goal;

    @Column(name = "NCP_WEIGHT_KG", nullable = true, columnDefinition = "decimal(10,2)")
    private Double weightKg;

    @Column(name = "NCP_NOCONFORM_KG", nullable = true, columnDefinition = "decimal(10,2)")
    private Double noconformKg;

    @Column(name = "NCP_TOTAL_PRODUCTION_KG", nullable = true, columnDefinition = "decimal(10,2)")
    private Double totalProductionKg;

    @Column(name = "NCP_PERCENT_MONTHLY", nullable = true, columnDefinition = "decimal(19,2)")
    private Double percentMonthly;

    @Column(name = "NCP_TOTAL_SALES", nullable = true, columnDefinition = "decimal(10,2)")
    private Double totalSales;

    @Column(name = "NCP_GOAL_MONTHLY", nullable = true, columnDefinition = "decimal(5,2)")
    private Double goalMonthly;

    @Column(name = "NCP_PERCENT_NO_QUALITY", nullable = true, columnDefinition = "decimal(19,2)")
    private Double percentNoQuality;

    @Column(name = "NCP_DETAIL_OTHER", nullable = true, columnDefinition = "varchar(max)")
    private String detailOther;

    @Column(name = "NCP_ADDITIONAL_REMARKS", nullable = true, columnDefinition = "varchar(max)")
    private String aditionalRemarks;


    @Column(name = "NCP_ASUSER", nullable = true)
    private String asUser;

    @Column(name = "NCP_U_NAME", nullable = false, length = 1024)
    private String userName;

    @Column(name = "NCP_JOB", nullable = false, length = 512)
    private String job;

    @Column(name = "NCP_WORK_AREA", nullable = false, length = 512)
    private String workArea;

    @Column(name = "NCP_STATE", nullable = true)
    private String state;

    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadMedida unit;

    public String getAffectedLine() {
        return affectedLine;
    }

    public void setAffectedLine(String affectedLine) {
        this.affectedLine = affectedLine;
    }

    public Integer getIdNCP() {
        return idNCP;
    }

    public String getSourceLine() {
        return sourceLine;
    }

    public void setSourceLine(String sourceLine) {
        this.sourceLine = sourceLine;
    }

    public void setIdNCP(Integer idNCP) {
        this.idNCP = idNCP;
    }

    public LocalDateTime getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(LocalDateTime dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public LocalDate getDateDetection() {
        return dateDetection;
    }

    public void setDateDetection(LocalDate dateDetection) {
        this.dateDetection = dateDetection;
    }

    public LocalDate getDateProduction() {
        return dateProduction;
    }

    public void setDateProduction(LocalDate dateProduction) {
        this.dateProduction = dateProduction;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getOrderProduction() {
        return orderProduction;
    }

    public void setOrderProduction(String orderProduction) {
        this.orderProduction = orderProduction;
    }

    public String getHccFreeUse() {
        return hccFreeUse;
    }

    public void setHccFreeUse(String hccFreeUse) {
        this.hccFreeUse = hccFreeUse;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getAmountProduced() {
        return amountProduced;
    }

    public void setAmountProduced(Double amountProduced) {
        this.amountProduced = amountProduced;
    }

    public Double getAmountNonConforming() {
        return amountNonConforming;
    }

    public void setAmountNonConforming(Double amountNonConforming) {
        this.amountNonConforming = amountNonConforming;
    }

    public String getUnitNCP() {
        return this.unit != null ? this.unit.getAbreviatura() : "";
    }

    public void setUnitNCP(String unitNCP) {
        this.unitNCP = unitNCP;
    }

    public Boolean getExistsInventory() {
        if (this.existingMaterial > 0)
            return true;
        else
            return false;
    }

    public void setExistsInventory(Boolean existsInventory) {
        this.existsInventory = existsInventory;
    }

    public Double getValidityPercent() {
        return validityPercent;
    }

    public void setValidityPercent(Double validityPercent) {
        this.validityPercent = validityPercent;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getPercentPNC() {
        return percentPNC;
    }

    public void setPercentPNC(Double percentPNC) {
        this.percentPNC = percentPNC;
    }

    public Double getGoal() {
        return goal;
    }

    public void setGoal(Double goal) {
        this.goal = goal;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public Double getNoconformKg() {
        return noconformKg;
    }

    public void setNoconformKg(Double noconformKg) {
        this.noconformKg = noconformKg;
    }

    public Double getTotalProductionKg() {
        return totalProductionKg;
    }

    public void setTotalProductionKg(Double totalProductionKg) {
        this.totalProductionKg = totalProductionKg;
    }

    public Double getPercentMonthly() {
        return percentMonthly;
    }

    public void setPercentMonthly(Double percentMonthly) {
        this.percentMonthly = percentMonthly;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }

    public Double getGoalMonthly() {
        return goalMonthly;
    }

    public void setGoalMonthly(Double goalMonthly) {
        this.goalMonthly = goalMonthly;
    }

    public Double getPercentNoQuality() {
        return percentNoQuality;
    }

    public void setPercentNoQuality(Double percentNoQuality) {
        this.percentNoQuality = percentNoQuality;
    }

    public String getAditionalRemarks() {
        return aditionalRemarks;
    }

    public void setAditionalRemarks(String aditionalRemarks) {
        this.aditionalRemarks = aditionalRemarks;
    }

    public String getAsUser() {
        return asUser;
    }

    public void setAsUser(String asUser) {
        this.asUser = asUser;
    }

    public Double getDaysAntiquities() {
        return daysAntiquities;
    }

    public void setDaysAntiquities(Double daysAntiquities) {
        this.daysAntiquities = daysAntiquities;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getFiveM() {
        return fiveM;
    }

    public void setFiveM(String fiveM) {
        this.fiveM = fiveM;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getRepTitle() {
        return repTitle;
    }

    public void setRepTitle(String repTitle) {
        this.repTitle = repTitle;
    }

    public String getRepSubtitle() {
        return repSubtitle;
    }

    public void setRepSubtitle(String repSubtitle) {
        this.repSubtitle = repSubtitle;
    }

    public String getRepReview() {
        return repReview;
    }

    public void setRepReview(String repReview) {
        this.repReview = repReview;
    }

    public String getRepRegister() {
        return repRegister;
    }

    public void setRepRegister(String repRegister) {
        this.repRegister = repRegister;
    }

    public String getRepCode() {
        return repCode;
    }

    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    public String getRepReference() {
        return repReference;
    }

    public void setRepReference(String repReference) {
        this.repReference = repReference;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public String getDetailOther() {
        return detailOther;
    }

    public void setDetailOther(String detailOther) {
        this.detailOther = detailOther;
    }

    public List<ExitMaterialHistory> getExitMaterialH() {
        return exitMaterialH;
    }

    public void setExitMaterialH(List<ExitMaterialHistory> exitMaterialH) {
        this.exitMaterialH = exitMaterialH;
    }

    public String getDefect() {
        return defect;
    }

    public void setDefect(String defect) {
        this.defect = defect;
    }

    public Double getExistingMaterial() {
        return existingMaterial;
    }

    public void setExistingMaterial(Double existingMaterial) {
        this.existingMaterial = existingMaterial;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UnidadMedida getUnit() {
        return unit;
    }

    public void setUnit(UnidadMedida unit) {
        this.unit = unit;
    }
}
