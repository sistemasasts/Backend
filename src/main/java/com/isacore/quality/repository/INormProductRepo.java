package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.NormProduct;
import com.isacore.quality.model.NormProductPK;


@Repository
public interface INormProductRepo extends JpaRepository<NormProduct, NormProductPK> {

}
