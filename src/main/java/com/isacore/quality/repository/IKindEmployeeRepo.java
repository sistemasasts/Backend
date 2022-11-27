package com.isacore.quality.repository;

import com.isacore.sgc.acta.model.KindEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IKindEmployeeRepo extends JpaRepository<KindEmployee, String>{

}
