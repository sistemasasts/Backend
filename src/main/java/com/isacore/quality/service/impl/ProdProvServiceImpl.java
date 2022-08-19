package com.isacore.quality.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.dto.ProdProvDTO;
import com.isacore.quality.model.ProdProv;
import com.isacore.quality.model.ProdProvPK;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.Provider;
import com.isacore.quality.repository.IProdProvRepo;
import com.isacore.quality.repository.IProviderRepo;
import com.isacore.quality.service.IProdProvService;

@Service
public class ProdProvServiceImpl implements IProdProvService {

	@Autowired
	private IProdProvRepo repo;

	@Autowired
	private IProviderRepo repoProvider;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<ProdProv> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProdProv create(ProdProv pp) {
		return this.repo.save(pp);
	}

	@Override
	public ProdProv findById(ProdProv pp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProdProv update(ProdProv pp) {
		return this.repo.save(pp);
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

	@Override
	public Integer validateProdProv(Integer idProduct, Integer idProveedor) {

		List<Object> list = this.repo.validateProdProv(idProduct, idProveedor);

		if (list == null || list.isEmpty())
			return null;
		else
			return (Integer) list.get(0);

	}

	@Override
	public List<ProdProvDTO> findByProduct(Product product) {

		logger.info("Obteniendo los proveedores del producto: " + product.toString());

		List<ProdProv> providers = repo.findByProductOrderByProvider_NameProviderAsc(product);

		logger.info("Proveedores obtenidos: " + providers.size());

		return providers.stream()
				.map(x -> new ProdProvDTO(x.getProduct().getIdProduct(), x.getProvider().getIdProvider(),
						x.getProvider().getNameProvider(), x.getAsUser(), x.getStatus(), x.getTypeProv()))
				.collect(Collectors.toList());
	}

	@Override
	public int deleteByProductAndProvider(Integer idProduct, Integer IdProvider) {
		try {
			Product product = new Product();
			product.setIdProduct(idProduct);
			Provider provider = new Provider();
			provider.setIdProvider(IdProvider);
			ProdProvPK pk = new ProdProvPK();
			pk.setProduct(product);
			pk.setProvider(provider);

			logger.info("Eliminando al proveedor: " + IdProvider);

			this.repo.deleteByProductAndProvider(idProduct, IdProvider);
			return 0;
		} catch (Exception ex) {
			logger.info("Error: " + ex.getMessage());
			return 1;
		}
	}

	@Override
	public List<Provider> findProviderAssignNot(Product product) {

		logger.info("Buscar proveedores asignados al producto:" + product.toString());

		List<ProdProv> proveedores = this.repo.findByProductOrderByProvider_NameProviderAsc(product);

		logger.info("Proveedores asignados :" + proveedores.size());

		List<Integer> idsProveedoresAsignados = new ArrayList<>();
		proveedores.forEach(x -> {
			idsProveedoresAsignados.add(x.getProvider().getIdProvider());
		});

		List<Provider> proveedoresNoAsignados = new ArrayList<>();
		
		if(idsProveedoresAsignados.isEmpty())
			proveedoresNoAsignados = this.repoProvider.findAll();
		else
			proveedoresNoAsignados = this.repoProvider.findByIdProviderNotIn(idsProveedoresAsignados);

		logger.info("Proveedores no asignados :" + proveedoresNoAsignados.size());

		return proveedoresNoAsignados;
	}

	@Override
	public int createProdProv(ProdProvDTO dto) {

		logger.info("Asignando al nuevo Proveedor :" + dto.toString());
		final String status= "VIGENTE";
		this.repo.createProdProv(dto.getIdProduct(), dto.getIdProvider(), status, dto.getTypeProv(),
				dto.getAsUser(), LocalDateTime.now());

		return 0;
	}

	@Override
	public int updateProdProv(ProdProvDTO dto) {

		logger.info("Actualizando al Proveedor asignado :" + dto.toString());

		this.repo.updateProdProv(dto.getStatus().toString(), dto.getTypeProv(), dto.getAsUser(), LocalDateTime.now(),
				dto.getIdProduct(), dto.getIdProvider());

		return 0;
	}

}
