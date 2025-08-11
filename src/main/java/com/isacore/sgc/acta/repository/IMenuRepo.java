package com.isacore.sgc.acta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isacore.security.model.Menu;

public interface IMenuRepo extends JpaRepository<Menu, Integer> {

	
}
