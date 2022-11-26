package com.isacore.quality.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class GeneradorContrasena {

	private PasswordEncoder passwordEncoder;

	@Autowired
	public GeneradorContrasena(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public boolean coinciden(String contrasenaNuevaTextoPlano, String contrasenaActualCifrada) {
		return passwordEncoder.matches(contrasenaNuevaTextoPlano, contrasenaActualCifrada);
	}

	public String generar(String contrasenaTextoPlano) {
		return passwordEncoder.encode(contrasenaTextoPlano);
	}
}
