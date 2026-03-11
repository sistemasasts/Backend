package com.isacore.quality.repository.reclamoMP;

import java.util.List;

import com.isacore.RepositorioBase;
import com.isacore.quality.model.reclamoMP.ComplaintEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.isacore.quality.model.reclamoMP.Complaint;
import org.springframework.data.repository.query.Param;

public interface IComplaintRepo extends RepositorioBase<Complaint> {
	
	@Query(value = "select *from complaint c \r\n" + 
			"inner join provider v on c.com_idprovider=v.prov_id\r\n" + 
			"inner join product p on p.product_id=c.com_idproduct;\r\n" + 
			"", nativeQuery = true)
	List<Object[]> findAllCustomize();
	
	@Query(value = "SELECT NEXT VALUE FOR complaint_secuence", nativeQuery = true)
	int secuencialSiguiente();

	List<Complaint> findByStateAndAprobadorCalidad(ComplaintEstado estado, String usuario);

	List<Complaint> findByStateAndAprobadorCompras(ComplaintEstado estado, String usuario);

	@Query(value = "select DISTINCT a.* from complaint(nolock) a inner join\n" +
			"provider_actionplan(nolock)b on  a.id = b.com_id\n" +
			"where b.estado in ('ASIGNADA','REGRESADO') AND B.pap_responsable= :responsable", nativeQuery = true)
	List<Complaint> findByPlanesAccionPorUsuarioSesion(@Param("responsable")String responsable);
}