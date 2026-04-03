package com.inspire.msusuarios.service.implementacion;

import com.inspire.mscommon.abstracts.EstadoAbstract;
import com.inspire.mscommon.criteria.SearchSpecification;
import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityConflictException;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityNotFoundException;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dao.usuarios.RolDao;
import com.inspire.msusuarios.dto.request.RolRequest;
import com.inspire.msusuarios.model.usuarios.RolEquipo;
import com.inspire.msusuarios.service.contratos.RolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RolEquipoServiceImpl implements RolService {
    private final RolDao rolDao;

    public RolEquipoServiceImpl(RolDao rolDao) {
        this.rolDao = rolDao;
    }

    public RolEquipo getRol(String rolId) {
        return rolDao.findById(rolId).orElseThrow(() -> {
            log.error("No existe el rol");
            return new EntityNotFoundException("No existe el rol");
        });
    }

    public Page<RolEquipo> listarRoles(Integer pagina, Integer cantidad) {
        Pageable pageable = PageRequest.of(pagina, cantidad);
        return rolDao.findAllByEstadoOrderByFecCre(EstadoAbstract.ACTIVO, pageable);
    }

    @Override
    public Page<RolEquipo> listarSearchRoles(SearchRequest searchRequest) {
        SearchSpecification<RolEquipo> specificationRol = new SearchSpecification<>(searchRequest);
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        log.info("Rol: {}", rolDao.findAll(specificationRol, pageable));
        return rolDao.findAll(specificationRol, pageable);
    }

    public RolEquipo crearRol(RolRequest rolRequest, Transaccion transaction) {
        if (!rolDao.existsByNombre(rolRequest.getNombre())) {
            RolEquipo rolEquipo = RolEquipo.builder()
                    .nombre(rolRequest.getNombre())
                    .descripcion(rolRequest.getDescripcion())
                    .estado(EstadoAbstract.ACTIVO)
                    .usuCre(transaction.getTrUsuarioId())
                    .fecCre(transaction.getTrFecha())
                    .usuMod(transaction.getTrUsuarioId())
                    .fecMod(transaction.getTrFecha())
                    .build();
            return rolDao.save(rolEquipo);
        } else {
            log.error("Rol con ese nombre {} ya existe", rolRequest.getNombre());
            throw new EntityConflictException("El rol con el nombre " + rolRequest.getNombre() + " ya existe");
        }
    }

    public RolEquipo editarRol(String rolId, RolRequest rolRequest, Transaccion transaccion) {
        RolEquipo editarRolEquipo = rolDao.findById(rolId).orElseThrow(() -> {
            log.error("No existe el rol con id: {}", rolId);
            return new EntityNotFoundException("No existe el rol con id: " + rolId);
        });
        if (!rolDao.existsByNombre(rolRequest.getNombre())) {
            editarRolEquipo.setNombre(rolRequest.getNombre());
            editarRolEquipo.setDescripcion(rolRequest.getDescripcion());
            editarRolEquipo.setUsuMod(transaccion.getTrUsuarioId());
            editarRolEquipo.setFecMod(transaccion.getTrFecha());
            return rolDao.save(editarRolEquipo);
        } else {
            log.error("El rol con id: {} se encuentra eliminado", rolId);
            throw new EntityConflictException("El rol con id: " + rolId + " se encuentra eliminado");
        }
    }

    public EliminarResponse eliminarRol(String rolId) {
        RolEquipo rolEquipo = rolDao.findById(rolId).orElseThrow(() -> {
            log.error("no existe el rol con id: {}", rolId);
            return new EntityNotFoundException("No existe el rol con id: " + rolId);
        });
        if (rolEquipo.getEstado().equals(EstadoAbstract.INACTIVO)) {
            log.error("El rol con ID '{}' ya ha sido eliminado previamente.", rolId);
            throw new EntityConflictException("El rol con ID '" + rolId + "' ya ha sido eliminado previamente.");
        } else {
            rolEquipo.setEstado(EstadoAbstract.INACTIVO);
            rolDao.save(rolEquipo);
            log.info("El rol con ID '{}' ha sido eliminado exitosamente.", rolId);
            return new EliminarResponse(rolId, "El rol ha sido eliminado exitosamente.");
        }
    }
}