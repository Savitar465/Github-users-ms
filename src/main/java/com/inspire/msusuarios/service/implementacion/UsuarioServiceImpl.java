package com.inspire.msusuarios.service.implementacion;

import com.inspire.mscommon.abstracts.EstadoAbstract;
import com.inspire.mscommon.criteria.SearchSpecification;
import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityConflictException;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityNotFoundException;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dao.usuarios.UsuarioDao;
import com.inspire.msusuarios.dto.request.UsuarioRequest;
import com.inspire.msusuarios.model.sgsidb.UsuariosSgsi;
import com.inspire.msusuarios.model.usuarios.Usuario;
import com.inspire.msusuarios.service.contratos.CargoAreaService;
import com.inspire.msusuarios.service.contratos.KeycloakService;
import com.inspire.msusuarios.service.contratos.UsuarioService;
import com.inspire.msusuarios.service.contratos.sgsidb.UsersService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {
    private final KeycloakService keycloakService;
    private final UsuarioDao usuarioDao;
    private final CargoAreaService cargoAreaService;
    private final UsersService usersService;


    public UsuarioServiceImpl(UsersService usersService, UsuarioDao usuarioDao, KeycloakService keycloakService, CargoAreaService cargoAreaService) {
        this.usuarioDao = usuarioDao;
        this.keycloakService = keycloakService;
        this.cargoAreaService = cargoAreaService;
        this.usersService = usersService;
    }

    @Override
    public Page<Usuario> listarUsuarios(Integer pagina, Integer cantidad) {
        Pageable pageable = PageRequest.of(pagina, cantidad);
        return usuarioDao.findAllByEstadoOrderByFecCre(EstadoAbstract.ACTIVO, pageable);
    }

    @Override
    public Usuario getUsuario(String usuarioKyId) {
        return usuarioDao.findById(usuarioKyId).orElseThrow(() -> {
            log.error("No existe usuario con el id: {}", usuarioKyId);
            return new EntityNotFoundException("No existe usuario con el id: " + usuarioKyId);
        });
    }

    @Override
    public Usuario crearUsuario(@Valid UsuarioRequest usuarioRequest, Transaccion transaction) {
        if (usuarioDao.existsByUsername(usuarioRequest.getUsername())) {
            log.error("Usuario con username {} ya existe", usuarioRequest.getUsername());
            throw new EntityConflictException("Usuario con username " + usuarioRequest.getUsername() + " ya existe");
        }
        UserRepresentation userRepresentation = keycloakService.crearUsuarioKeycloak(usuarioRequest);
        if (userRepresentation == null) {
            log.error("Error al crear el usuario en Keycloak");
            throw new EntityConflictException("Error al crear el usuario en Keycloak");
        }
        UsuariosSgsi usuariosSgsi = usersService.crearUsers(usuarioRequest, transaction);
        transaction.setTrUsuarioId(userRepresentation.getId());
        Usuario usuario = Usuario.builder()
                .usuarioKyId(userRepresentation.getId())
                .idSgsi(usuariosSgsi.getId())
                .email(usuarioRequest.getEmail())
                .username(usuarioRequest.getUsername())
                .nombres(usuarioRequest.getNombres())
                .apellidos(usuarioRequest.getApellidos())
                .estado(EstadoAbstract.ACTIVO)
                .usuCre(transaction.getTrUsuarioId())
                .fecCre(transaction.getTrFecha())
                .usuMod(transaction.getTrUsuarioId())
                .fecMod(transaction.getTrFecha()).build();

        return usuarioDao.save(usuario);
    }

    @Override
    public EliminarResponse eliminarUsuario(String usuarioKyId) {
        Usuario eliminarUsuario = usuarioDao.findById(usuarioKyId).orElseThrow(() -> {
            log.error("El usuario con ID '{}' no existe en el sistema.", usuarioKyId);
            return new EntityNotFoundException("El usuario con ID '" + usuarioKyId + "' no existe en el sistema.");
        });
        if (eliminarUsuario.getEstado().equals(EstadoAbstract.INACTIVO)) {
            log.error("El usuario con ID '{}' ya ha sido eliminado previamente.", usuarioKyId);
            throw new EntityConflictException("El usuario con ID '" + usuarioKyId + "' ya ha sido eliminado previamente.");
        } else {
            eliminarUsuario.setEstado(EstadoAbstract.INACTIVO);
            usuarioDao.save(eliminarUsuario);
            log.info("El usuario con ID '{}' ha sido eliminado exitosamente.", usuarioKyId);
            return new EliminarResponse(usuarioKyId, "El usuario ha sido eliminado exitosamente.");
        }
    }

    @Override
    public Usuario editarUsuario(String usuarioKyId, UsuarioRequest usuarioRequest, Transaccion transaccion) {
        Usuario editarUsuario = obtenerUsernamePorIdUsuario(usuarioKyId);
        usersService.editarUsers(editarUsuario.getIdSgsi(), usuarioRequest, transaccion);
        if (editarUsuario.getEstado().equals(EstadoAbstract.INACTIVO)) {
            log.error("El usuario con id: {} se encuentra eliminado", usuarioKyId);
            throw new EntityConflictException("El usuario con id: " + usuarioKyId + " se encuentra eliminado");
        } else {
            editarUsuario.setUsername(usuarioRequest.getUsername());
            editarUsuario.setEmail(usuarioRequest.getEmail());
            editarUsuario.setNombres(usuarioRequest.getNombres());
            editarUsuario.setApellidos(usuarioRequest.getApellidos());
            if(usuarioRequest.getCargoAreaId()!=null){
                editarUsuario.setCargoArea(cargoAreaService.getCargoArea(usuarioRequest.getCargoAreaId()));
            }
            editarUsuario.setUsuMod(transaccion.getTrUsuarioId());
            editarUsuario.setFecMod(transaccion.getTrFecha());
             Usuario prueba = usuarioDao.save(editarUsuario);
             log.info(prueba.getUsuarioKyId());
             return prueba;
        }
    }

    public Usuario obtenerUsernamePorIdUsuario(String usuarioId) {
        return usuarioDao.findById(usuarioId).orElseThrow(() -> {
            log.error("No existe el usuario con el id:{}", usuarioId);
            return new EntityNotFoundException("No existe el usuario con id: " + usuarioId);
        });
    }

    @Override
    public Page<Usuario> listarUsuariosSearch(SearchRequest searchRequest) {
        SearchSpecification<Usuario> specification = new SearchSpecification<>(searchRequest);
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        log.info("Usuario: {}", usuarioDao.findAll(specification, pageable));
        return usuarioDao.findAll(specification, pageable);
    }
}