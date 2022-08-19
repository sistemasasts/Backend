package com.isacore.quality.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.isacore.quality.dto.ReportPncDTO;
import com.isacore.quality.model.ExitMaterialHistory;
import com.isacore.quality.model.NonconformingProduct;
import com.isacore.quality.repository.IExitMaterialHistoryRepo;
import com.isacore.quality.repository.INonconformingProduct;
import com.isacore.quality.service.INonconformingProductService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class NonconformingProductServiceImpl implements INonconformingProductService{

	@Autowired
	private INonconformingProduct repo;
	
	@Autowired
	private IExitMaterialHistoryRepo repoExitMaterial;
	
	@Override
	public List<NonconformingProduct> findAll() {
		return this.repo.findAll();
	}

	@Override
	public NonconformingProduct create(NonconformingProduct ncp) {
		ncp.setRepCode("PNC-03");
		ncp.setRepReference("MP-PNC.01");
		ncp.setRepRegister("PNC.03");
		ncp.setRepReview("REV.01");
		ncp.setRepSubtitle("Tratamiento del Producto No Conforme");
		ncp.setRepTitle("FORMATO DE REGISTRO");
		return this.repo.save(ncp);
	}

	@Override
	public NonconformingProduct findById(NonconformingProduct ncp) {
		Optional<NonconformingProduct> o = this.repo.findById(ncp.getIdNCP());
		if(o.isPresent())
			return o.get();
		else
			return null;
	}

	@Override
	public NonconformingProduct update(NonconformingProduct ncp) {
		return this.repo.save(ncp);
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

	@Override
	public void consumeMaterialNC(Integer ncpId, Double quantity) {
		Optional<NonconformingProduct> ncpFound= repo.findById(ncpId);
		if(ncpFound.isPresent()) {
			
			Double stock= ncpFound.get().getExistingMaterial() - quantity;
			if(stock>=0) {
				ncpFound.get().setExistingMaterial(stock);
				this.repo.save(ncpFound.get());
			}
					
		}
			
	}

	@Override
	public void eliminarMaterilaConsumido(Integer ncpId, Double quantity) {
		Optional<NonconformingProduct> ncpFound= repo.findById(ncpId);
		if(ncpFound.isPresent()) {
			
			Double stock= ncpFound.get().getExistingMaterial() + quantity;			
				ncpFound.get().setExistingMaterial(stock);
				this.repo.save(ncpFound.get());
			
					
		}
		
	}

	@Override
	public byte[] generarReportePnc(Integer id) {
		
		byte [] data = null;
		
		
		try {
			//File file = new ClassPathResource("C:\\CRIMPTEK\\Calidad\\ReportPrpt\\PNCP.jasper").getFile();
			String ruta="C:\\CRIMPTEK\\Calidad\\ReportPrpt\\PNCP.jasper";
			JasperPrint print = JasperFillManager.fillReport(ruta, null,new JRBeanCollectionDataSource(Arrays.asList( this.obtenerDataParaReporte(id))));
			data = JasperExportManager.exportReportToPdf(print);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	private ReportPncDTO obtenerDataParaReporte(Integer id) {
		
		Optional<NonconformingProduct> pnc = this.repo.findById(id);
		List<ExitMaterialHistory> historial = this.repoExitMaterial.findByNcpID(id);
		
		ReportPncDTO report = new ReportPncDTO();
		
		if(pnc.isPresent()) {			
			
			report.setId(pnc.get().getIdNCP());
			report.setNombreProducto(pnc.get().getProduct().getNameProduct());
			report.setCantidad(pnc.get().getAmountNonConforming());
			report.setUnidad(pnc.get().getUnitNCP());
			report.setFechaDeteccion(pnc.get().getDateDetection());
			report.setFechaProduccion(pnc.get().getDateProduction());
			report.setLote(pnc.get().getBatch());
			report.setEntradaTraspaso(pnc.get().getHccFreeUse());
			report.setAreaInvolucrada(pnc.get().getArea().getNameArea());
			report.setPesoTotalNC(pnc.get().getWeightKg());
			report.setDefectos(pnc.get().getDefect());
			report.setObservaciones5m(pnc.get().getFiveM());
			report.setElaboradoPor(pnc.get().getUserName());
			report.setValdidezPorcentaje(pnc.get().getValidityPercent());
			report.setOrdenProduccion(pnc.get().getOrderProduction());
			report.setProcedenciaProducto(pnc.get().getSource());
			report.setListaSalidaMaterial(historial);			
			
		}
		
		return report;
	}

}
