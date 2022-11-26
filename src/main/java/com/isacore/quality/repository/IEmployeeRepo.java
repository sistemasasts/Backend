package com.isacore.quality.repository;

import com.isacore.sgc.acta.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEmployeeRepo extends JpaRepository<Employee, String>{

    Optional<Employee> findByCiEmployee(String identificacion);
}
