package com.inspire.msusuarios.service.implementacion;

import com.inspire.mscommon.abstracts.EstadoAbstract;
import com.inspire.mscommon.criteria.SearchSpecification;
import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityConflictException;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityNotFoundException;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dao.usuarios.EquipoDao;
import com.inspire.msusuarios.dto.request.EquipoRequest;
import com.inspire.msusuarios.dto.response.EquipoUsuariosResponse;
import com.inspire.msusuarios.mapper.EquipoMapper;
import com.inspire.msusuarios.model.usuarios.Equipo;
import com.inspire.msusuarios.service.contratos.EquipoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class EquipoServiceImpl implements EquipoService {
    private final EquipoDao equipoDao;

    @Autowired
    public EquipoServiceImpl(EquipoDao equipoDao) {
        this.equipoDao = equipoDao;

    }

    @Override
    public Equipo crearEquipo(EquipoRequest equipoRequest, Transaccion transaccion) {
        if (equipoDao.existsByNombre(equipoRequest.getNombre())) {
            log.error("Equipo con nombre {} ya existe", equipoRequest.getNombre());
            throw new EntityConflictException("Equipo con nombre " + equipoRequest.getNombre() + " ya existe");
        }
        Equipo equipo = new Equipo();
        equipo.setNombre(equipoRequest.getNombre());
        equipo.setDescripcion(equipoRequest.getDescripcion());
        equipo.setTipo(equipoRequest.getTipo());
        equipo.setEstado(EstadoAbstract.ACTIVO);
        equipo.setFecCre(transaccion.getTrFecha());
        equipo.setFecMod(transaccion.getTrFecha());
        equipo.setUsuCre(transaccion.getTrUsuarioId());
        equipo.setUsuMod(transaccion.getTrUsuarioId());
        if (equipoRequest.getEquipoPadreId() != null) {
            Equipo padre = equipoDao.findById(equipoRequest.getEquipoPadreId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipo padre no encontrado"));
            equipo.setEquipoPadre(padre);
        }
        Equipo equipoGuardado = equipoDao.save(equipo);
        String delimitador = "/";
        String pathId = (equipo.getEquipoPadre() != null)
                ? equipo.getEquipoPadre().getPathId() + delimitador + equipoGuardado.getEquipoId()
                : String.valueOf(equipoGuardado.getEquipoId());
        equipoGuardado.setPathId(pathId);
        return equipoDao.save(equipoGuardado);
    }

    public Page<Equipo> listarEquipos(Integer pagina, Integer cantidad) {
        Pageable pageable = PageRequest.of(pagina, cantidad);
        return equipoDao.findAllByEstadoOrderByFecCre(EstadoAbstract.ACTIVO, pageable);
    }

    public Page<Equipo> listarSearchEquipo(SearchRequest searchRequest) {
        SearchSpecification<Equipo> equipoSearchSpecification = new SearchSpecification<>(searchRequest);
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        log.info("Equipo: {}", equipoDao.findAll(equipoSearchSpecification, pageable));
        return equipoDao.findAll(equipoSearchSpecification, pageable);
    }

    public EliminarResponse eliminarEquipo(String equipoId) {
        Equipo editarEquipo = equipoDao.findById(equipoId)
                .orElseThrow(() -> {
                    log.error("No existe el equipo con id: {}", equipoId);
                    return new EntityNotFoundException("No existe el equipo con id: " + equipoId);
                });
        if (editarEquipo.getEstado().equals(EstadoAbstract.ACTIVO)) {
            editarEquipo.setEstado(EstadoAbstract.INACTIVO);
            equipoDao.save(editarEquipo);
            log.info("Se elimino el equipo con id: {}", equipoId);
            return new EliminarResponse(equipoId, "Se eliminó el equipo");
        } else {
            log.error("El equipo con id: {} se encuentra suspendido", equipoId);
            throw new EntityConflictException("El equipo con id: " + equipoId + " ya se encuentra eliminado");
        }
    }

    public Equipo editarEquipo(String equipoId, EquipoRequest equipoRequest, Transaccion transaccion) {
        Equipo editarEquipo = equipoDao.findById(equipoId)
                .orElseThrow(() -> {
                    log.error("El equipo con id: {} no existe", equipoId);
                    return new EntityNotFoundException("No existe el equipo con id: " + equipoId);
                });
        if (editarEquipo.getEstado().equals(EstadoAbstract.ACTIVO)) {
            editarEquipo.setNombre(equipoRequest.getNombre());
            editarEquipo.setDescripcion(equipoRequest.getDescripcion());
            editarEquipo.setFecMod(transaccion.getTrFecha());
            editarEquipo.setUsuMod(transaccion.getTrUsuarioId());
            return equipoDao.save(editarEquipo);
        } else {
            log.error("El equipo con id: {} se encuentra eliminado", equipoId);
            throw new EntityConflictException("El equipo con id: " + equipoId + " se encuentra eliminado");
        }
    }

    public EquipoUsuariosResponse getEquipoDetalle(String equipoId) {
        EquipoUsuariosResponse equipoUsuariosResponse = new EquipoUsuariosResponse();
        equipoUsuariosResponse.setEquipoResponse(EquipoMapper.INSTANCE.equipoToEquipoResponse(obtenerEquipo(equipoId)));
        return equipoUsuariosResponse;
    }

    public Equipo obtenerEquipo(String equipoId) {
        return equipoDao.findById(equipoId).orElseThrow(() -> {
            log.error("El equipo {} no existe:", equipoId);
            return new EntityNotFoundException("No existe el equipo con id: " + equipoId);
        });
    }

}
