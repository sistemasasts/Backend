package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.NonconformingProduct;

@Repository
public interface INonconformingProduct extends JpaRepository<NonconformingProduct, Integer>{

}
