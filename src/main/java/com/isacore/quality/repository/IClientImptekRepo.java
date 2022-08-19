package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.ClientImptek;

@Repository
public interface IClientImptekRepo extends JpaRepository<ClientImptek, Integer>{

}
