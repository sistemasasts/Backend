package com.isacore.sgc.acta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.sgc.acta.model.Committee;

@Repository
public interface ICommitteeRepo extends JpaRepository<Committee, Integer>{

}
