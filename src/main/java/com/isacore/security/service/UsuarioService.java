package com.isacore.security.service;

import com.isacore.quality.model.Area;
import com.isacore.quality.repository.IAreasRepo;
import com.isacore.security.dto.UsuarioDTO;
import com.isacore.security.dto.UsuarioPerfilDTO;
import com.isacore.security.exception.*;
import com.isacore.security.mapper.UsuarioMapper;
import com.isacore.security.mapper.UsuarioPerfilMapper;
import com.isacore.security.model.Perfil;
import com.isacore.security.model.Usuario;
import com.isacore.security.model.UsuarioEstadoEnum;
import com.isacore.security.model.UsuarioPerfil;
import com.isacore.security.repository.PerfilRepositorio;
import com.isacore.security.repository.UsuarioPerfilRepositorio;
import com.isacore.security.repository.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioPerfilRepositorio usuarioPerfilRepositorio;
    private final PerfilRepositorio perfilRepositorio;
    private final UsuarioPerfilMapper usuarioPerfilMapper;
    private final IAreasRepo areaRepositorio;

    public UsuarioService(
            UsuarioRepositorio usuarioRepositorio,
            UsuarioMapper usuarioMapper,
            UsuarioPerfilRepositorio usuarioPerfilRepositorio,
            PerfilRepositorio perfilRepositorio,
            UsuarioPerfilMapper usuarioPerfilMapper,
            IAreasRepo areaRepositorio
    ) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioMapper = usuarioMapper;
        this.usuarioPerfilRepositorio = usuarioPerfilRepositorio;
        this.perfilRepositorio = perfilRepositorio;
        this.usuarioPerfilMapper = usuarioPerfilMapper;
        this.areaRepositorio = areaRepositorio;
    }

    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        Optional<Usuario> userOp = usuarioRepositorio.findByNombreUsuario(usuarioDTO.getNombreUsuario());
        if (userOp.isPresent()) {
            throw new UsuarioUnicoException("El usuario " + usuarioDTO.getNombreUsuario() + " ya esta registrado.");
        }

        try {
            Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
            usuario.setEstado(UsuarioEstadoEnum.ACTIVO);
            Usuario savedUser = usuarioRepositorio.save(usuario);
            return usuarioMapper.toDTO(savedUser);
        } catch (RuntimeException rte) {
            throw new CrearUsuarioException("No se pudo crear el usuario con la informacion: " + usuarioDTO, rte);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioDTO listarUsuarioPorId(Long id) {
        Usuario userOp = this.obtenerUsuarioPorId(id);
        UsuarioDTO usuarioDTO = usuarioMapper.toDTO(userOp);
        //userResponseDTO.setRol(roles);
        return usuarioDTO;
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioActivoPorNombreUsuario(String nombreUsuario) {
        Optional<Usuario> userOp = usuarioRepositorio.findByNombreUsuario(nombreUsuario);
        if (userOp.isPresent()) {
            Usuario usuario = userOp.get();
            if (UsuarioEstadoEnum.ACTIVO.equals(usuario.getEstado())) {
                return usuarioMapper.toDTO(usuario);
            }
        }
        throw new UsuarioNotFoundException("El usuario " + nombreUsuario + " no existe o no es activo.");
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosActivos() {
        List<Usuario> activeUsers = usuarioRepositorio.findByEstado(UsuarioEstadoEnum.ACTIVO);
        return usuarioMapper.toDTOLista(activeUsers);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuarios() {
        List<Usuario> allUsers = usuarioRepositorio.findAll();
        return usuarioMapper.toDTOLista(allUsers);
    }

    private Usuario obtenerUsuarioPorId(long id) {
        return usuarioRepositorio.findById(id).orElseThrow(() ->
                new RecursoNotFoundException("El usuario con id:" + id + " no existe."));
    }

    @Transactional(readOnly = true)
    public List<UsuarioPerfilDTO> obtenerPerfilesPorUsuarioId(long userId) {
        List<UsuarioPerfil> usuarioPerfils = this.usuarioPerfilRepositorio.findByUsuarioId(userId);
        return usuarioPerfilMapper.toDTOList(usuarioPerfils);
    }

    public Usuario obtenerPorNombreUsuario(String nombreUsuario){
        return usuarioRepositorio.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RecursoNotFoundException("Usuario no encontrado",409));
    }


    public UsuarioDTO actualizarUsuario(UsuarioDTO userRequestDTO) {
        if (userRequestDTO.getId() == null) {
            throw new IllegalArgumentException("El ID del usuario es requerido para la actualización.");
        }

        try {
            Usuario userToUpdate = this.obtenerUsuarioPorId(userRequestDTO.getId());
            userToUpdate.setEmail(userRequestDTO.getEmail());
            userToUpdate.setNumeroIdentificacion(userRequestDTO.getNumeroIdentificacion());
            userToUpdate.setNombre(userRequestDTO.getNombre());
            userToUpdate.setTrabajo(userRequestDTO.getTrabajo());
            userToUpdate.setEstado(userRequestDTO.getEstado());
            if (userRequestDTO.getArea() != null) {
                Area area = this.areaRepositorio.findById((int)userRequestDTO.getArea().getIdArea()).orElseThrow(
                        () -> new RecursoNotFoundException("Area no encontrada con ID: " + userRequestDTO.getArea().getIdArea()));
                userToUpdate.setArea(area);
            }
            this.usuarioRepositorio.save(userToUpdate);
            return usuarioMapper.toDTO(userToUpdate);
        } catch (Exception rte) {
            throw new ActualizarUsuarioException("No se pudo actualizar el usuario:  " + userRequestDTO.getId(), rte);
        }
    }

    public UsuarioPerfilDTO crearUsuarioPerfil(UsuarioPerfilDTO obj) {

        if (this.usuarioPerfilRepositorio.findByUsuarioIdAndPerfilId(obj.getUsuarioId(), obj.getPerfilId()).isPresent()) {
            String userName = usuarioRepositorio.findById(obj.getUsuarioId()).map(Usuario::getNombreUsuario).orElse("Desconocido");
            String profileName = perfilRepositorio.findById(obj.getPerfilId()).map(Perfil::getNombre).orElse("Desconocido");
            throw new EntityExistsException("Perfil asignado previamente. Usuario: " + userName + ", Perfil: " + profileName);
        }

        Usuario user = this.obtenerUsuarioPorId(obj.getUsuarioId());
        Perfil perfil = perfilRepositorio.findById(obj.getPerfilId())
                .orElseThrow(() -> new RecursoNotFoundException("Perfil no encontrado con ID: " + obj.getPerfilId()));

        UsuarioPerfil userProfile = new UsuarioPerfil();
        userProfile.setUsuarioId(user.getId());
        userProfile.setPerfilId(perfil.getId());
        userProfile.setActivo(true);

        return this.usuarioPerfilMapper.toDTO(usuarioPerfilRepositorio.save(userProfile));
    }

    public Map<String, Object> eliminarPerfilPorUsuarioIdPerfilId(Long usuarioId, Long perfilId) {

        Optional<UsuarioPerfil> userProfile = usuarioPerfilRepositorio.findByUsuarioIdAndPerfilId(usuarioId, perfilId);
        if (!userProfile.isPresent()) {
            throw new RecursoNotFoundException("Vínculo User-Profile no encontrado para usuario: " + usuarioId + " y perfil: " + perfilId);
        }
        try {
            usuarioPerfilRepositorio.delete(userProfile.get());
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("status", true);
            resultado.put("message", "El proceso se completó correctamente");
            return resultado;
        } catch (RuntimeException rte) {
            throw new EliminarUsuarioPerfilException("No se pudo eliminar Vínculo Usuario Perfil para usuario: " + usuarioId + " y perfil: " + perfilId, rte);
        }
    }
}
