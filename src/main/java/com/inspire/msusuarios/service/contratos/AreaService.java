package com.inspire.msusuarios.service.contratos;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dto.request.AreaRequest;
import com.inspire.msusuarios.model.usuarios.Area;
import org.springframework.data.domain.Page;

public interface AreaService {
    /**
     * Metodo que lista las áreas por módulo
     *
     * @param pagina   número de pagina [0..n]
     * @param cantidad cantidad de items por página
     * @return retorna una lista de áreas
     */
    Page<Area> listarAreas(Integer pagina, Integer cantidad);

    /**
     * Metodo para obtener un área
     *
     * @param areaId identificador de Area
     * @return retorna el área response
     */
    Area getArea(String areaId);

    /**
     * Metodo que crea area
     *
     * @param areaRequest recibe un array de area request
     * @param transaccion recibe la información de la transacción
     * @return etorna un area response creado
     */
    Area crearArea(AreaRequest areaRequest, Transaccion transaccion);

    /**
     * Metodo para editar información de área
     *
     * @param areaId      identificador del área
     * @param areaRequest recibe un array de area request
     * @param transaccion recibe un array de area request
     * @return retorna un area response editado
     */
    Area editarArea(String areaId, AreaRequest areaRequest, Transaccion transaccion);

    /**
     * Metodo que elimina áreas
     *
     * @param areaId identificador del área
     */
    EliminarResponse eliminarArea(String areaId);

    /**
     * Metodo que asigna roles a todos los usuarios con los Ids del parámetro
     *
     * @param searchRequest recibe un array de áreas
     * @return retorna una lista con los usuarios con los roles asignados
     */
    Page<Area> listarSearchAreas(SearchRequest searchRequest);
}
