package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.Family;

@Repository
public interface IFamilyRepo extends JpaRepository<Family, Integer>{

}
