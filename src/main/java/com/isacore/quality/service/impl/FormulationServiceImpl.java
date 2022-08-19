package com.isacore.quality.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.FormulaList;
import com.isacore.quality.model.Formulation;
import com.isacore.quality.repository.IFormulationRepo;
import com.isacore.quality.service.IFormulationService;

@Service
public class FormulationServiceImpl implements IFormulationService {

	@Autowired
	private IFormulationRepo repo;

	@Override
	public List<Formulation> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Formulation create(Formulation obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Formulation findById(Formulation id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Formulation update(Formulation obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

	@Override
	public List<Formulation> findFormulationByProductAndFormType(String idPSap, Integer idF) {
		List<Object[]> response = this.repo.searchFormulationByProductFormuType(idPSap, idF);
		List<Formulation> formulations = new ArrayList<>();

		if (response == null || response.isEmpty())
			return formulations;
		else {
			response.forEach(x -> {
				Formulation f = new Formulation();
				f.setComponent((String) x[0]);
				f.setMultiFactor((Integer)x[1]);
				f.setUnit((String)x[2]);
				f.setValue((x[3]) == null ? null: ((BigDecimal)x[3]).doubleValue());
				f.setUnit("kg");
				formulations.add(f);
			});

			return formulations;
		}
	}

	@Override
	public Boolean validateExistFormulation(Integer idFi, Integer idFl, Integer idProd) {
		
		List<Object> list = this.repo.validateExistFormulation(idFi, idFl, idProd);
		
		if(list == null)
			return false;
		else
			if(list.isEmpty())
				return false;
			else
				return true;

	}

	@Override
	public int createFormulation(Formulation f, String user) {
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
        String dateUpdate = now.format(formatter);
        String dateUpdateE = f.getDateUpdateExcel().format(formatter2);
		
        this.repo.createFormulation(f.getFormulationItem().getIdFItem(), f.getFormulationList().getIdFormula(), f.getProduct().getIdProduct(), f.getMultiFactor(), f.getValue(), dateUpdateE, dateUpdate, user);
		return 0;
	}

	@Override
	public int updateFormulation(Formulation f, String user) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
        String dateUpdate = now.format(formatter);
        String dateUpdateE = f.getDateUpdateExcel().format(formatter2);
		
        this.repo.updateFormulation(f.getMultiFactor(), f.getValue(), dateUpdateE, dateUpdate, user, f.getFormulationItem().getIdFItem(), f.getFormulationList().getIdFormula(), f.getProduct().getIdProduct());        
		return 0;
	}

	@Override
	public List<FormulaList> getTypeFormulationsListByProduct(Integer idProd) {
		
		List<FormulaList> formulaList = new ArrayList<>();
		List<Object[]> list = this.repo.getTypeFormulationsListByProduct(idProd);
		
		if(!list.isEmpty())
			list.forEach(x -> {
				FormulaList fl = new FormulaList();
				fl.setIdFormula((Integer) x[0]);
				fl.setDescription((String)x[1]);
				formulaList.add(fl);
			});
		
		return formulaList;
	}

}
