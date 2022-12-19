package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "area")
@Table(name = "AREA")
public class Area {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AREA_ID")
	private Integer idArea;

	@Column(name = "AREA_NAME", nullable = false, length = 512)
	private String nameArea;

	@Column(columnDefinition = "bit default 0")
	private boolean activo;
    @Column(columnDefinition = "bit default 0")
    private boolean activoPruebasProceso;

	public Integer getIdArea() {
		return idArea;
	}

	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}

	public String getNameArea() {
		return nameArea;
	}

	public void setNameArea(String nameArea) {
		this.nameArea = nameArea;
	}

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isActivoPruebasProceso() {
        return activoPruebasProceso;
    }

    public void setActivoPruebasProceso(boolean activoPruebasProceso) {
        this.activoPruebasProceso = activoPruebasProceso;
    }
}
