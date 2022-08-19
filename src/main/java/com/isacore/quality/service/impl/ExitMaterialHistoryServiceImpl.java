package com.isacore.quality.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.dto.OutputMaterialDTO;
import com.isacore.quality.model.ExitMaterialHistory;
import com.isacore.quality.model.NonconformingProduct;
import com.isacore.quality.repository.IExitMaterialHistoryRepo;
import com.isacore.quality.repository.INonconformingProduct;
import com.isacore.quality.service.IExitMaterialHistoryService;
import com.isacore.quality.service.INonconformingProductService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;

@Service
public class ExitMaterialHistoryServiceImpl implements IExitMaterialHistoryService {

	@Autowired
	private IExitMaterialHistoryRepo repo;
	
	@Autowired
	private IUserImptekRepo repoUser;
	
	@Autowired
	private INonconformingProduct repoPNC;
	
	@Autowired
	private INonconformingProductService pncService;
	
	@Override
	public List<ExitMaterialHistory> findAll() {
		
		return this.repo.findAll();
	}

	@Override
	public ExitMaterialHistory create(ExitMaterialHistory obj) {
		
		Optional<UserImptek> user = repoUser.findById(obj.getAsUser());
		if(user.isPresent()) {
			obj.setNameUser(user.get().getEmployee().getCompleteName());
			obj.setJob(user.get().getEmployee().getJob());
			obj.setWorkArea(user.get().getEmployee().getArea().getNameArea());
		}
		
		ExitMaterialHistory objCreated = this.repo.save(obj);
		
		if(objCreated!= null) {
			pncService.consumeMaterialNC(objCreated.getNcpID(), objCreated.getQuantity());
		}
		return objCreated;
	}

	@Override
	public ExitMaterialHistory findById(ExitMaterialHistory obj) {
		Optional<ExitMaterialHistory> com = this.repo.findById(obj.getIdEMH());
		if (com.isPresent())
			return com.get();
		else
			return null;
	}

	@Override
	public ExitMaterialHistory update(ExitMaterialHistory obj) {
		Optional<UserImptek> user = repoUser.findById(obj.getAsUser());
		if(user.isPresent()) {
			obj.setNameUser(user.get().getEmployee().getCompleteName());
			obj.setJob(user.get().getEmployee().getJob());
			obj.setWorkArea(user.get().getEmployee().getArea().getNameArea());
		}
		return this.repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		Optional<ExitMaterialHistory> obj = this.repo.findById(Integer.parseInt(id));
		if(obj.isPresent()) {
			this.pncService.eliminarMaterilaConsumido(obj.get().getNcpID(), obj.get().getQuantity());
			this.repo.deleteById(Integer.parseInt(id));
		}
		return true;
	}

	@Override
	public OutputMaterialDTO buscarPorIdPNC(Integer id) {
		
		List<ExitMaterialHistory> historial = this.repo.findByNcpID(id);
		Optional<NonconformingProduct> pnc = repoPNC.findById(id);
		if(pnc.isPresent()) {
			
			OutputMaterialDTO salidaMaterial = new OutputMaterialDTO(
					id, 
					pnc.get().getProduct().getNameProduct(), 
					pnc.get().getProduct().getTypeProductTxt(), 
					pnc.get().getAmountNonConforming(), 
					pnc.get().getExistingMaterial(), 
					pnc.get().getUnitNCP(), 
					historial);
			return salidaMaterial;
		}
		
		
		return new OutputMaterialDTO();
	}

}
