package com.inspire.msusuarios.service.contratos;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dto.request.RolRequest;
import com.inspire.msusuarios.model.usuarios.RolEquipo;
import org.springframework.data.domain.Page;

public interface RolService {
    /**
     * Método que obtiene un rol por id
     *
     * @param rolId identificador de rol
     * @return retorna un rol
     */
    RolEquipo getRol(String rolId);

    /**
     * Método que lista todos los roles
     *
     * @param pagina   número de pagina [0..n]
     * @param cantidad cantidad de items por página
     * @return retorna una lista de roles
     */
    Page<RolEquipo> listarRoles(Integer pagina, Integer cantidad);

    /**
     * Método que crea rol
     *
     * @param rolRequest  recibe un array de rol request
     * @param transaction recibe la información de la transacción
     * @return retorna un rol response creado
     */
    RolEquipo crearRol(RolRequest rolRequest, Transaccion transaction);

    /**
     * Método que edita roles
     * @param rolRequest recibe un array de rol request
     * @param transaccion    recibe la información de la transacción
     * @return retorna una lista con los roles asignados
     * @param rolId identificador del rol
     */
    RolEquipo editarRol(String rolId, RolRequest rolRequest, Transaccion transaccion);

    /**
     * Método que elimina roles
     *
     * @param rolId identificador del rol
     */
    EliminarResponse eliminarRol(String rolId);

    /**
     * Metodo que lista los roles por criterios de busqueda
     *
     * @param searchRequest objeto que contiene los criterios de busqueda
     * @return retorna una lista con los roles
     */
    Page<RolEquipo> listarSearchRoles(SearchRequest searchRequest);
}
