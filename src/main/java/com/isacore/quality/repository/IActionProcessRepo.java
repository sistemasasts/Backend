package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.ActionProcess;

@Repository
public interface IActionProcessRepo extends JpaRepository<ActionProcess, String> {

}
