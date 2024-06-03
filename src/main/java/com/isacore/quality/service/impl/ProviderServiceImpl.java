package com.isacore.quality.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.isacore.quality.dto.ProductoDto;
import com.isacore.quality.dto.ProveedorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.exception.ProveedorEliminarErrorException;
import com.isacore.quality.model.Provider;
import com.isacore.quality.repository.IProviderRepo;
import com.isacore.quality.service.IProviderService;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional(readOnly = true)
	@Override
	public  List<ProveedorDto> listarPorNombreCriterio(String criterio) {
		List<ProveedorDto> productos = this.repo.findByNameProviderContaining(criterio)
				.stream()
				.map(x -> new ProveedorDto(x.getIdProvider(), x.getNameProvider(), x.getDescProvider(),x.getTypeProvider()))
				.sorted(Comparator.comparing(ProveedorDto::getNameProvider))
				.collect(Collectors.toList());
		return productos;
	}

}
