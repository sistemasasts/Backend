package com.isacore.sgc.acta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.isacore.security.model.Usuario;
import com.isacore.security.model.UsuarioEstadoEnum;
import com.isacore.security.repository.UsuarioRepositorio;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepositorio repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = repo.findByNombreUsuario(username).orElse(null);
		
		if(usuario == null) {
			throw new UsernameNotFoundException(String.format("Usuario no existe", username));
		}
		
		if(usuario.getEstado() != UsuarioEstadoEnum.ACTIVO) {
			 //TODO: falta validar si el usuario esta habilitado para ingresar a la plataforma
		}
		
		List<GrantedAuthority> roles = new ArrayList<>();
		 
//		roles.add(new SimpleGrantedAuthority(usuario.getRole().getRolName()));
		/*usuario.getRoles().forEach(rol -> {
			roles.add(new SimpleGrantedAuthority(rol.getNombre()));
		});*/
		
		UserDetails ud = new User(usuario.getNombreUsuario(), usuario.getContrasena(), roles);
		return ud;
		
	}

}
