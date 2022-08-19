package com.isacore.quality.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.model.ProdProv;
import com.isacore.quality.model.ProdProvPK;
import com.isacore.quality.model.Product;

@Repository
public interface IProdProvRepo extends JpaRepository<ProdProv, ProdProvPK>{
	
	@Query(value = "select pp.prov_id from prod_prov pp where pp.product_id = :idProduct and pp.prov_id = :idProveedor", nativeQuery = true)
	List<Object> validateProdProv(@Param("idProduct")Integer idProduct, @Param("idProveedor")Integer idProveedor);

	@Transactional
	@Modifying
	@Query(value = "insert into prod_prov(product_id, prov_id, pp_status, pp_type, pp_asuser, pp_update) values (:idProduct, :idProveedor, :statusP, :typeP, :asuser, :updateP)", nativeQuery = true)
	int createProdProv(@Param("idProduct")Integer idProduct, @Param("idProveedor")Integer idProveedor, @Param("statusP")String statusP, @Param("typeP")String typeP, @Param("asuser")String asuser,@Param("updateP")LocalDateTime updateP );
	
	@Transactional
	@Modifying
	@Query(value = "update prod_prov set pp_status = :statusP, pp_type = :typeP, pp_asuser =:asuser, pp_update =:updateP where product_id = :idProduct and prov_id = :idProveedor", nativeQuery = true)
	int updateProdProv(@Param("statusP")String statusP, @Param("typeP")String typeP,@Param("asuser")String asuser,@Param("updateP")LocalDateTime updateP, @Param("idProduct")Integer idProduct, @Param("idProveedor")Integer idProveedor);
	
	List<ProdProv> findByProductOrderByProvider_NameProviderAsc(Product product);
	
	@Transactional(propagation = Propagation.NESTED)
	@Modifying
	@Query(value = "delete prod_prov where product_id = :idProduct and prov_id = :idProvider", nativeQuery = true)
	int deleteByProductAndProvider(@Param("idProduct") Integer idProduct, @Param("idProvider")Integer idProvider);
}
