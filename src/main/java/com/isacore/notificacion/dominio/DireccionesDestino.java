package com.isacore.notificacion.dominio;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesCadena.*;

@NoArgsConstructor
@Data
public class DireccionesDestino {
	
	private Set<String> direccionesA = new HashSet<>();
	private Set<String> direccionesCC = new HashSet<>();
	private Set<String> direccionesCCO = new HashSet<>();

	public DireccionesDestino(Set<String> direccionesA) {
		direccionesA.forEach((direccion) -> agregarDireccionA(direccion));
	}
	
	public DireccionesDestino(Set<String> direccionesA, Set<String> direccionesCC) {
		direccionesA.forEach((direccion) -> agregarDireccionA(direccion));
		direccionesCC.forEach((direccion) -> agregarDireccionCC(direccion));
	}
	
	public DireccionesDestino(String direccionA) {
		agregarDireccionA(direccionA);
	}

	public DireccionesDestino(String direccionA, String direccionCC) {
		agregarDireccionA(direccionA);
		agregarDireccionCC(direccionCC);
	}
	
	public DireccionesDestino(String direccionA, String direccionCC, String direccionCCO) {
		agregarDireccionA(direccionA);		
		agregarDireccionCC(direccionCC);
		agregarDireccionCCO(direccionCCO);
	}

	public void agregarDireccionA(String direccionA) {
		if (noEsNuloNiBlanco(direccionA)) {			
			direccionesA.add(direccionA);
		}
	}
	
	public void agregarDireccionCC(String direccionCC) {
		if (noEsNuloNiBlanco(direccionCC)) {
			direccionesCC.add(direccionCC);
		}
	}
	
	public void agregarDireccionCCO(String direccionCCO) {
		if (noEsNuloNiBlanco(direccionCCO)) {
			direccionesCCO.add(direccionCCO);
		}
	}

	private String descripcion(Set<String> direcciones) {
		if (!direcciones.isEmpty()) {
			return direcciones.stream().collect(Collectors.joining(", "));
		} else {
			return "<No especificado>";
		}
	}
	
	public String getDescripcionDireccionesA() {
		return descripcion(direccionesA);
	}
	
	public String getDescripcionDireccionesCC() {
		return descripcion(direccionesCC);
	}
	
	public String getDescripcionDireccionesCCO() {
		return descripcion(direccionesCCO);
	}

	public Set<String> getDireccionesA() {
		return Collections.unmodifiableSet(direccionesA);
	}

	public Set<String> getDireccionesCC() {
		return Collections.unmodifiableSet(direccionesCC);
	}

	public Set<String> getDireccionesCCO() {
		return Collections.unmodifiableSet(direccionesCCO);
	}

	public boolean isTieneDireccionesA() {
		return !direccionesA.isEmpty();
	}
	
	public boolean isTieneDireccionesCC() {
		return !direccionesCC.isEmpty();
	}
	
	public boolean isTieneDireccionesCCO() {
		return !direccionesCCO.isEmpty();
	}

}
