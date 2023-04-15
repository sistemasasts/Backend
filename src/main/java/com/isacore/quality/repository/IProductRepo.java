package com.isacore.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.Product;

@Repository
public interface IProductRepo extends JpaRepository<Product, Integer> {

//	@Query(value = "select p.idProduct, p.sapCode, p.nameProduct, p.descProduct, p.itcdq, p.familyProduct, p.typeProduct from product p where p.idProduct = :idP", nativeQuery = true)
//	List<Object[]> findOnlyProductById(@Param("idP") Integer idP);

    @Query(value = "select p.product_id, p.product_sap_code, p.product_name, p.product_description, p.product_type, pl.propl_id, pl.propl_name, rtrim(replace(prop.property_type, char(9),'')) as property_type, pl.propl_periodicity, prop.property_norm, prop.property_min, prop.property_max, prop.unit_id, prop.property_view, prop.property_view_hcc  from product p  inner join property prop on p.product_id = prop.product_id inner join propertylist pl on prop.propl_id = pl.propl_id where p.product_id = :idP and pl.propl_periodicity = :period", nativeQuery = true)
    List<Object[]> findProductByIdAndPeriod(@Param("idP") Integer idP, @Param("period") String period);

    @Query(value = "select p.product_id, p.product_sap_code, p.product_name, f.fea_length, f.fea_length_unit, f.fea_gross_weight, f.fea_gross_weight_unit, f.fea_net_weight, f.fea_net_weight_unit, f.fea_weight_area, f.fea_umb, f.fea_unit_cost, f.fea_distributor_price from product p  inner join feature f on p.fea_id = f.fea_id where p.product_id = :idP", nativeQuery = true)
    List<Object[]> findProductFeature(@Param("idP") Integer idP);

    @Query(nativeQuery = true, value = "select p.product_id, p.product_sap_code, p.product_name, p.product_description, p.product_itcdq, p.product_type,f.fam_id, f.fam_name,lp.lp_id, lp.lp_name from product p full join family f on p.fam_id = f.fam_id full join line_production lp on p.lp_id = lp.lp_id")
    List<Object[]> findAllProducts();

    @Query(value = "select p.product_id, p.product_sap_code, p.product_name, p.product_description, p.product_type, pl.propl_id, pl.propl_name, rtrim(replace(prop.property_type, char(9),'')) as property_type, pl.propl_periodicity, prop.property_norm, prop.property_min, prop.property_max, prop.property_unit, prop.property_view, prop.property_view_hcc  from product p  inner join property prop on p.product_id = prop.product_id inner join propertylist pl on prop.propl_id = pl.propl_id where p.product_id = :idP and pl.propl_id = :idProperty", nativeQuery = true)
    List<Object[]> findProductByIdAndIdProperty(@Param("idP") Integer idP, @Param("idProperty") String idProperty);

    @Query(value = "select p.product_id, p.product_sap_code, p.product_name, p.product_description, p.product_type, pl.propl_id, pl.propl_name, rtrim(replace(prop.property_type, char(9),'')) as property_type, pl.propl_periodicity, prop.property_norm, prop.property_min, prop.property_max, prop.unit_id, prop.property_view, prop.property_view_hcc, p.product_typetxt  from product p  inner join property prop on p.product_id = prop.product_id inner join propertylist pl on prop.propl_id = pl.propl_id where p.product_id = :idP ", nativeQuery = true)
    List<Object[]> findProductPropertiesByIdProduct(@Param("idP") Integer idP);

    List<Product> findByNameProductContaining(String criterio);

    List<Product> findByTypeProductTxtInAndNameProductContaining(List<String> grupos, String criterio);

}
