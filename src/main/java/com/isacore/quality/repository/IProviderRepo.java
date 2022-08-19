package com.isacore.quality.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.Provider;

@Repository
public interface IProviderRepo extends JpaRepository<Provider, Integer>{

	@Query(value = "select p.prov_id, p.prov_name, p.prov_type from prod_prov pp inner join provider p on pp.prov_id = p.prov_id where pp.product_id = :idP", nativeQuery = true)
	List<Object[]> findByProductId(@Param("idP")Integer idP);
	
	@Query(value = "select p.prov_id, p.prov_name, p.prov_type from prod_prov pp inner join provider p on pp.prov_id = p.prov_id where pp.pp_status='vigente' and pp.product_id = :idP", nativeQuery = true)
	List<Object[]> findByProductIdVigente(@Param("idP")Integer idP);
	
	List<Provider> findByIdProviderNotIn(Collection<Integer> idProvider);
}
