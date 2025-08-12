package com.isacore.security.service;

import com.isacore.security.dto.PerfilDTO;
import com.isacore.security.exception.RecursoNotFoundException;
import com.isacore.security.mapper.PerfilMapper;
import com.isacore.security.model.Perfil;
import com.isacore.security.repository.PerfilRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PerfilService {

    private final PerfilRepositorio perfilRepositorio;
    private final PerfilMapper perfilMapper;

    public PerfilService(PerfilRepositorio perfilRepositorio, PerfilMapper perfilMapper) {
        this.perfilRepositorio = perfilRepositorio;
        this.perfilMapper = perfilMapper;
    }

    @Transactional(readOnly = true)
    public List<PerfilDTO> listar() {
        List<Perfil> profiles = this.perfilRepositorio.findAllByOrderByNombre();
        return perfilMapper.toDTOList(profiles);
    }

    @Transactional
    public PerfilDTO crearPerfil(PerfilDTO perfilDTO) {

        if (perfilRepositorio.findByNombre(perfilDTO.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un perfil con el nombre: '" + perfilDTO.getNombre() + "'.");
        }
        Perfil perfil = perfilMapper.toEntity(perfilDTO);
        perfil.setActivo(true);
        this.perfilRepositorio.save(perfil);
        return perfilMapper.toDTO(perfil);
    }

    @Transactional(readOnly = true)
    public List<PerfilDTO> obtenerPerfilesActivos() {
        List<Perfil> profiles = this.perfilRepositorio.findByActivoTrueOrderByNombre();
        return perfilMapper.toDTOList(profiles);
    }

    @Transactional
    public PerfilDTO actualizarPerfil(PerfilDTO profileRequestDTO) {
        final Perfil existingProfile = this.perfilRepositorio.findById(profileRequestDTO.getId())
                .orElseThrow(() -> new RecursoNotFoundException("Perfil con ID: " + profileRequestDTO.getId() + " no encontrado."));

        if (!existingProfile.getNombre().equalsIgnoreCase(profileRequestDTO.getNombre())) {
            perfilRepositorio.findByNombre(profileRequestDTO.getNombre()).ifPresent(p -> {
                if (p.getId() != profileRequestDTO.getId()) {
                    throw new IllegalArgumentException("Ya existe un perfil con el nombre: '" + profileRequestDTO.getNombre() + "'.");
                }
            });
        }

        Perfil updatedProfile = this.perfilRepositorio.save(existingProfile);
        return perfilMapper.toDTO(updatedProfile);
    }
}
