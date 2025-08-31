package com.isacore.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class UtilidadesSeguridad {

	private static final Authentication authentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	private final static boolean authenticationCorrespondeAUsuarioAnonimo(Authentication authentication) {
		return authentication.getName().equals("anonymousUser");
	}
	
	public static final boolean esUsuarioAnonimo() {
		final Authentication authentication = authentication();
		return (authentication != null) && (authenticationCorrespondeAUsuarioAnonimo(authentication));
	}
	
	public static final boolean esUsuarioIdentificado() {
		final Authentication authentication = authentication();
		return (authentication != null) && (!authenticationCorrespondeAUsuarioAnonimo(authentication));
	}

	public static final String nombreUsuarioEnSesion() {
		Jwt detalles = authentication() == null ? null : (Jwt) authentication().getPrincipal();
		return detalles != null ? detalles.getClaims().get("preferred_username").toString().split("@")[0] : "anonymousUser";
	}

	public static final Jwt usuarioEnSesion() {
		return authentication() == null ? null : (Jwt) authentication().getPrincipal();
	}
	
	private UtilidadesSeguridad() {}
	
}
