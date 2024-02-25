package com.isacore.quality.repository.pnc;

import com.isacore.RepositorioBase;
import com.isacore.quality.model.pnc.Defecto;
import com.isacore.quality.model.pnc.PncSalidaConcesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPncSalidaConcesionRepo extends RepositorioBase<PncSalidaConcesion> {

}
