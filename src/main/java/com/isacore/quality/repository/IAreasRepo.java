package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.Area;

import java.util.List;

@Repository
public interface IAreasRepo extends JpaRepository<Area, Integer>{

    List<Area> findByActivoPruebasProcesoTrue();
}
