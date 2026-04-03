package com.inspire.msusuarios.service.implementacion.sgsidb;

import com.inspire.mscommon.abstracts.EstadoAbstract;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityConflictException;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityNotFoundException;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dao.sgsidb.UsersDao;
import com.inspire.msusuarios.dto.request.UsuarioRequest;
import com.inspire.msusuarios.model.sgsidb.UsuariosSgsi;
import com.inspire.msusuarios.service.contratos.sgsidb.UsersService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsersServiceImpl implements UsersService {
    private final UsersDao usersDao;

    public UsersServiceImpl(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public Page<UsuariosSgsi> listarUsers(Integer pagina, Integer cantidad) {
        Pageable pageable = PageRequest.of(pagina, cantidad);
        return usersDao.findAll(pageable);
    }

    @Override
    public UsuariosSgsi crearUsers(@Valid UsuarioRequest usuarioRequest, Transaccion transaction) {
        if (usersDao.existsByUsername(usuarioRequest.getUsername())) {
            log.error("Usuario con username {} ya existe", usuarioRequest.getUsername());
            throw new EntityConflictException("Usuario con username " + usuarioRequest.getUsername() + " ya existe");
        }
        return usersDao.save(UsuariosSgsi.builder()
                .nombres(usuarioRequest.getNombres())
                .apellidos(usuarioRequest.getApellidos())
                .email(usuarioRequest.getEmail())
                .username(usuarioRequest.getUsername())
                .createdAt(transaction.getTrFecha())
                .updatedAt(transaction.getTrFecha())
                .estado(EstadoAbstract.ACTIVO)
                .build()
        );
    }

    @Override
    public UsuariosSgsi editarUsers(Long id, UsuarioRequest usuarioRequest, Transaccion transaccion) {
        UsuariosSgsi usuariosSgsi = usersDao.findById(id)
                .orElseThrow(() -> {
                    log.error("No existe el usuario con id: {}", id);
                    return new EntityNotFoundException("No existe el usuario con id: " + id);
                });
        if (usuariosSgsi.getEstado().equals(EstadoAbstract.INACTIVO)) {
            log.error("El usuario con id: {} se encuentra eliminado", id);
            throw new EntityConflictException("El usuario con id: " + id + " se encuentra eliminado");
        } else {
            usuariosSgsi.setUsername(usuarioRequest.getUsername());
            usuariosSgsi.setApellidos(usuarioRequest.getApellidos());
            usuariosSgsi.setNombres(usuarioRequest.getNombres());
            usuariosSgsi.setEmail(usuarioRequest.getEmail());
            usuariosSgsi.setUpdatedAt(transaccion.getTrFecha());
            return usersDao.save(usuariosSgsi);
        }
    }
}
