package com.inspire.msusuarios.service.implementacion;

import com.inspire.mscommon.abstracts.EstadoAbstract;
import com.inspire.mscommon.criteria.SearchSpecification;
import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityConflictException;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityNotFoundException;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dao.usuarios.SistemaDao;
import com.inspire.msusuarios.dto.request.SistemaRequest;
import com.inspire.msusuarios.model.usuarios.Sistema;
import com.inspire.msusuarios.service.contratos.SistemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SistemaServiceImpl implements SistemaService {
    private final SistemaDao sistemaDao;

    public SistemaServiceImpl(SistemaDao sistemaDao) {
        this.sistemaDao = sistemaDao;
    }

    @Override
    public Page<Sistema> listarSistemas(Integer pagina, Integer cantidad) {
        Pageable pageable = PageRequest.of(pagina, cantidad);
        return sistemaDao.findAllByEstadoOrderByFecCre(EstadoAbstract.ACTIVO,pageable);
    }
    @Override
    public Page<Sistema> listarSearchSistemas(SearchRequest searchRequest) {
        SearchSpecification<Sistema> specificationSistema = new SearchSpecification<>(searchRequest);
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        log.info("Sistema: {}", sistemaDao.findAll(specificationSistema, pageable));
        return sistemaDao.findAll(specificationSistema, pageable);
    }

    @Override
    public Sistema crearSistema(SistemaRequest sistemaRequest, Transaccion transaction) {
        if (sistemaDao.existsByNombre(sistemaRequest.getNombre())) {
            log.error("El nombre '{}' ya corresponde a un sistema existente.", sistemaRequest.getNombre());
            throw new EntityConflictException("Ya existe un sistema registrado con el nombre: " + sistemaRequest.getNombre());
        }
        Sistema sistema = Sistema.builder()
                .nombre(sistemaRequest.getNombre())
                .descripcion(sistemaRequest.getDescripcion())
                .estado(EstadoAbstract.ACTIVO)
                .usuCre(transaction.getTrUsuarioId())
                .usuMod(transaction.getTrUsuarioId())
                .fecCre(transaction.getTrFecha())
                .fecMod(transaction.getTrFecha())
                .build();
        return sistemaDao.save(sistema);
    }

    @Override
    public Sistema editarSistema(String sistemaId, SistemaRequest sistemaRequest, Transaccion transaction) {
        Sistema sistema = sistemaDao.findById(sistemaId)
                .orElseThrow(() -> {
                    log.error("No se encontró un sistema con el identificador '{}'.", sistemaId);
                    return new EntityNotFoundException("No existe un sistema registrado con el ID '" + sistemaId + "'.");
                });
        if (sistema.getEstado().equals(EstadoAbstract.INACTIVO)) {
            log.error("El sistema identificado por '{}' está marcado como inactivo o eliminado.", sistemaId);
            throw new EntityConflictException("La operación no puede completarse ya que el sistema con ID '" + sistemaId + "' está desactivado o eliminado.");
        } else {
            sistema.setNombre(sistemaRequest.getNombre());
            sistema.setDescripcion(sistemaRequest.getDescripcion());
            sistema.setUsuMod(transaction.getTrUsuarioId());
            sistema.setFecMod(transaction.getTrFecha());
            return sistemaDao.save(sistema);
        }
    }

    @Override
    public EliminarResponse eliminarSistema(String sistemaId) {
        Sistema sistema = sistemaDao.findById(sistemaId)
                .orElseThrow(() -> {
                    log.error("No se localizó un sistema con el ID proporcionado: '{}'.", sistemaId);
                    return new EntityNotFoundException("No se encontró un sistema en los registros con el identificador '" + sistemaId + "'.");
                });
        if (EstadoAbstract.INACTIVO.equals(sistema.getEstado())) {
            log.error("El sistema con ID '{}' ya está deshabilitado o eliminado previamente.", sistemaId);
            throw new EntityConflictException("El sistema que intenta eliminar, con ID '" + sistemaId + "', ya está marcado como inactivo.");
        } else {
            sistema.setEstado(EstadoAbstract.INACTIVO);
            sistemaDao.save(sistema);
            return new EliminarResponse(sistemaId, "El sistema ha sido desactivado exitosamente en los registros.");
        }
    }

    @Override
    public Sistema obtenerSistema(String sistemaId) {
        return sistemaDao.findById(sistemaId)
                .orElseThrow(() -> {
                    log.error("No se pudo recuperar el sistema con el identificador '{}'.", sistemaId);
                    return new EntityNotFoundException("El sistema asociado al ID '" + sistemaId + "' no se encuentra en los datos almacenados.");
                });
    }
}

