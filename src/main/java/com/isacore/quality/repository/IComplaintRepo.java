package com.isacore.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.isacore.quality.model.Complaint;

public interface IComplaintRepo extends JpaRepository<Complaint, Integer> {
	
	@Query(value = "select *from complaint c \r\n" + 
			"inner join provider v on c.com_idprovider=v.prov_id\r\n" + 
			"inner join product p on p.product_id=c.com_idproduct;\r\n" + 
			"", nativeQuery = true)
	List<Object[]> findAllCustomize();
	
	@Query(value = "SELECT NEXT VALUE FOR complaint_secuence", nativeQuery = true)
	int secuencialSiguiente();
}