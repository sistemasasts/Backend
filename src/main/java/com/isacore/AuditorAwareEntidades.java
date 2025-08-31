package com.isacore;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class AuditorAwareEntidades implements AuditorAware<String> {

    private static final int AUDITORIA_LONGITUD_USUARIO_MAX = 100;
    private static final String AUDITORIA_USUARIO_DESCONOCIDO = "desconocido";
    private static final String AUDITORIA_USUARIO_ANONIMO = "anonimo";

    @Override
    public Optional<String> getCurrentAuditor() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return Optional.of("Queue Process");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            return Optional.of(jwt.getClaimAsString("preferred_username"));
        } else if (principal instanceof String && principal.equals("anonymousUser")) {
            return Optional.of("Anonymous User");
        } else {
            return Optional.of("Unknown User");
        }
    }
}
