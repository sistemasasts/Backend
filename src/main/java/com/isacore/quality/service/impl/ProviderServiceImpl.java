package com.isacore.quality.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.exception.ProveedorEliminarErrorException;
import com.isacore.quality.model.Provider;
import com.isacore.quality.repository.IProviderRepo;
import com.isacore.quality.service.IProviderService;

@Service
public class ProviderServiceImpl implements IProviderService {

	@Autowired
	private IProviderRepo repo;

	@Override
	public List<Provider> findAll() {
		return this.repo.findAll();
	}

	@Override
	public Provider create(Provider obj) {
		return repo.save(obj);
	}

	@Override
	public Provider findById(Provider provider) {
		Optional<Provider> proveedor= repo.findById(provider.getIdProvider());
		return proveedor.isPresent() ? proveedor.get() : null;
	}

	@Override
	public Provider update(Provider obj) {
		return repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		try {
		repo.deleteById(Integer.parseInt(id));
		}catch (Exception e) {
			
			throw new ProveedorEliminarErrorException();
		}
		return true;

	}

	@Override
	public List<Provider> findByProductId(Integer idP) {

		List<Object[]> list = this.repo.findByProductId(idP);

		if (list.isEmpty() || list == null)
			return null;
		else {
			List<Provider> listProvider = new ArrayList<>();

			list.forEach((Object[] x) -> {
				Provider prov = new Provider();
				prov.setIdProvider((Integer) x[0]);
				prov.setNameProvider((String) x[1]);
				prov.setTypeProvider((String) x[2]);
				listProvider.add(prov);
			});
			return listProvider;
		}
	}

	@Override
	public List<Provider> findByProductIdVigente(Integer idP) {
		List<Object[]> list = this.repo.findByProductIdVigente(idP);

		if (list.isEmpty() || list == null)
			return null;
		else {
			List<Provider> listProvider = new ArrayList<>();

			list.forEach((Object[] x) -> {
				Provider prov = new Provider();
				prov.setIdProvider((Integer) x[0]);
				prov.setNameProvider((String) x[1]);
				prov.setTypeProvider((String) x[2]);
				listProvider.add(prov);
			});
			return listProvider;
		}
	}

}
