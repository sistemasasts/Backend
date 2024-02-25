package com.isacore;

import com.isacore.util.UtilidadesSeguridad;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareEntidades implements AuditorAware<String>   {

	private static final int AUDITORIA_LONGITUD_USUARIO_MAX = 100;
	private static final String AUDITORIA_USUARIO_DESCONOCIDO = "desconocido";
	private static final String AUDITORIA_USUARIO_ANONIMO = "anonimo";

	@Override
	public Optional<String> getCurrentAuditor() {

		if (UtilidadesSeguridad.esUsuarioIdentificado()) {
			return Optional.of(usuarioDeSesion());
		} else if (UtilidadesSeguridad.esUsuarioAnonimo()) {
			return Optional.of(AUDITORIA_USUARIO_ANONIMO);
		} else {
			return Optional.of(AUDITORIA_USUARIO_DESCONOCIDO);
		}
	}

	private final String usuarioDeSesion() {
		final String userDetailsNombreUsuario = UtilidadesSeguridad.usuarioEnSesion();
		final String usuario = String.format("%s", userDetailsNombreUsuario);
		return StringUtils.left(usuario, AUDITORIA_LONGITUD_USUARIO_MAX);
	}
}
