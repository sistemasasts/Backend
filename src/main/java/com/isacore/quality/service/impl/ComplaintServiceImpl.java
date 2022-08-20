package com.isacore.quality.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.model.Complaint;
import com.isacore.quality.model.Problem;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.Provider;
import com.isacore.quality.repository.IComplaintRepo;
import com.isacore.quality.repository.IProductRepo;
import com.isacore.quality.service.IComplaintService;
import com.isacore.quality.service.IProviderService;
import com.isacore.util.PassFileToRepository;

@Component
public class ComplaintServiceImpl implements IComplaintService {

	private static final Log LOG = LogFactory.getLog(ComplaintServiceImpl.class);
	
	@Autowired
	private IComplaintRepo repo;

	@Autowired
	private IProductRepo productRepo;

	@Autowired
	private IProviderService providerService;

	@Override
	public List<Complaint> findAll() {
		List<Complaint> cTmp = this.repo.findAll();
		if (!cTmp.isEmpty()) {
			for (Complaint complaint : cTmp) {
				Optional<Product> p = this.productRepo.findById(complaint.getIdProduct());
				Product aux = new Product();
				aux.setNameProduct(p.get().getNameProduct());
				aux.setUnit(p.get().getUnit());
				complaint.setProduct(aux);
				if(complaint.getIdProvider() != null){
					aux.setProviders(this.providerService.findByProductIdVigente(p.get().getIdProduct()));
					if(aux.getProviders() != null) {
						for (Provider provider : aux.getProviders()) {
							if (complaint.getIdProvider().compareTo(provider.getIdProvider()) == 0 )
								complaint.setProvider(provider);
						}
					}
				}
				
				for(Problem pr: complaint.getListProblems()){
					if(pr.getNameFileP()!=null) {
						String fb64=PassFileToRepository.fileToBase64(pr.getPictureStringB64(), pr.getExtensionFileP());
						if(fb64!=null ) {
							pr.setPictureStringB64(fb64);
						}
					}
					
				}
			}

		}
		return cTmp;
	}

	@Override
	public Complaint create(Complaint obj) {
		if(obj.getNumber() == 0) {
			obj.setNumber(this.repo.secuencialSiguiente());
		}		
		return this.repo.save(obj);
	}

	@Override
	public Complaint findById(Complaint obj) {
		Optional<Complaint> com = this.repo.findById(obj.getIdComplaint());
		if (com.isPresent()) {
			Optional<Product> p = this.productRepo.findById(com.get().getIdProduct());
			Product aux = new Product();
			aux.setNameProduct(p.get().getNameProduct());
			aux.setUnit(p.get().getUnit());
			com.get().setProduct(aux);
			if(com.get().getIdProvider() != null){
				aux.setProviders(this.providerService.findByProductIdVigente(p.get().getIdProduct()));
				for (Provider provider : aux.getProviders()) {
					if (com.get().getIdProvider().compareTo(provider.getIdProvider()) == 0)
						com.get().setProvider(provider);
				}
			}
			return com.get();
		} else {
			return null;
		}

	}

	@Override ////
	public Complaint update(Complaint obj) {
		return this.repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		return true;
	}

	@Override
	public List<Complaint> findAllCustomize() {
		return null;
	}

	@Override
	@Transactional
	public Complaint close(Integer complaitId) {
		Optional<Complaint> complaintOp = repo.findById(complaitId);
		if(complaintOp.isPresent()) {
			Complaint complaint = complaintOp.get();
			complaint.setState("Cerrado");
			complaint.setCloseDate(LocalDateTime.now());
			
			LOG.info(String.format("Reclamo de MP id=%s ha sido CERRADO", complaint.getIdComplaint()));
			return complaint;
		}
		return null;
	}

}
