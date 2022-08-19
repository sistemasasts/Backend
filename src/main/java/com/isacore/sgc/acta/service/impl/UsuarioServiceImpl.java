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

import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

	@Autowired
	private IUserImptekRepo repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserImptek usuario = repo.findOneByNickName(username);
		
		if(usuario == null) {
			throw new UsernameNotFoundException(String.format("Usuario no existe", username));
		}
		
		if(!usuario.getEmployee().getState()) {
			 //TODO: falta validar si el usuario esta habilitado para ingresar a la plataforma
		}
		
		List<GrantedAuthority> roles = new ArrayList<>();
		 
		roles.add(new SimpleGrantedAuthority(usuario.getRole().getRolName()));
		/*usuario.getRoles().forEach(rol -> {
			roles.add(new SimpleGrantedAuthority(rol.getNombre()));
		});*/
		
		UserDetails ud = new User(usuario.getNickName(), usuario.getUserPass(), roles);
		return ud;
		
	}

}
