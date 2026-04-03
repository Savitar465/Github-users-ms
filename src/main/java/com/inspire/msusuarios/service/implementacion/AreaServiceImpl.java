package com.inspire.msusuarios.service.implementacion;

import com.inspire.mscommon.abstracts.EstadoAbstract;
import com.inspire.mscommon.criteria.SearchSpecification;
import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityConflictException;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityNotFoundException;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dao.usuarios.AreaDao;
import com.inspire.msusuarios.dto.request.AreaRequest;
import com.inspire.msusuarios.model.usuarios.Area;
import com.inspire.msusuarios.service.contratos.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AreaServiceImpl implements AreaService {
    private static final String AREA_NO_EXISTE = "No existe el área con id: %s";
    private static final String AREA_YA_EXISTE = "Área con nombre %s ya existe";
    private static final String AREA_ELIMINADA = "El área con id: %s se encuentra eliminada";
    private static final String AREA_YA_ELIMINADA = "El área con id: %s ya se encuentra eliminada";
    private static final String AREA_ELIMINACION_EXITOSA = "Se eliminó el área con id: %s";
    private final AreaDao areaDao;

    public AreaServiceImpl(AreaDao areaDao) {
        this.areaDao = areaDao;
    }

    @Override
    public Page<Area> listarAreas(Integer pagina, Integer cantidad) {
        Pageable pageable = PageRequest.of(pagina, cantidad);
        return areaDao.findAllByEstadoOrderByFecCre(EstadoAbstract.ACTIVO, pageable);
    }

    @Override
    public Page<Area> listarSearchAreas(SearchRequest searchRequest) {
        SearchSpecification<Area> specification = new SearchSpecification<>(searchRequest);
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        log.info("Area: {}", areaDao.findAll(specification, pageable));
        return areaDao.findAll(specification, pageable);
    }

    public Area getArea(String areaId) {
        return areaDao.findById(areaId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(AREA_NO_EXISTE, areaId);
                    log.error(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    @Override
    public Area crearArea(AreaRequest areaRequest, Transaccion transaccion) {
        if (!areaDao.existsByNombre(areaRequest.getNombre())) {
            Area area = Area.builder()
                    .nombre(areaRequest.getNombre())
                    .descripcion(areaRequest.getDescripcion())
                    .estado(EstadoAbstract.ACTIVO)
                    .fecCre(transaccion.getTrFecha())
                    .fecMod(transaccion.getTrFecha())
                    .usuCre(transaccion.getTrUsuarioId())
                    .usuMod(transaccion.getTrUsuarioId())
                    .build();
            return areaDao.save(area);
        } else {
            String errorMessage = String.format(AREA_YA_EXISTE, areaRequest.getNombre());
            log.error(errorMessage);
            throw new EntityConflictException(errorMessage);
        }
    }

    public Area editarArea(String areaId, AreaRequest areaRequest, Transaccion transaccion) {
        Area editarArea = areaDao.findById(areaId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(AREA_NO_EXISTE, areaId);
                    log.error(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });

        if (editarArea.getEstado().equals(EstadoAbstract.ACTIVO)) {
            editarArea.setNombre(areaRequest.getNombre());
            editarArea.setDescripcion(areaRequest.getDescripcion());
            editarArea.setFecMod(transaccion.getTrFecha());
            editarArea.setUsuMod(transaccion.getTrUsuarioId());
            return areaDao.save(editarArea);
        } else {
            String errorMessage = String.format(AREA_ELIMINADA, areaId);
            log.error(errorMessage);
            throw new EntityConflictException(errorMessage);
        }
    }

    public EliminarResponse eliminarArea(String areaId) {
        Area editarArea = areaDao.findById(areaId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(AREA_NO_EXISTE, areaId);
                    log.error(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });

        if (editarArea.getEstado().equals(EstadoAbstract.ACTIVO)) {
            editarArea.setEstado(EstadoAbstract.INACTIVO);
            areaDao.save(editarArea);
            String infoMessage = String.format(AREA_ELIMINACION_EXITOSA, areaId);
            log.info(infoMessage);
            return new EliminarResponse(areaId, "Se eliminó el área");
        } else {
            String errorMessage = String.format(AREA_YA_ELIMINADA, areaId);
            log.error(errorMessage);
            throw new EntityConflictException(errorMessage);
        }
    }
}

