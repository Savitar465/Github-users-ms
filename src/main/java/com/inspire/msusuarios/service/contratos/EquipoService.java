package com.inspire.msusuarios.service.contratos;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dto.request.EquipoRequest;
import com.inspire.msusuarios.dto.response.EquipoUsuariosResponse;
import com.inspire.msusuarios.model.usuarios.Equipo;
import org.springframework.data.domain.Page;

public interface EquipoService {
    /**
     * Método para crear equipo
     *
     * @param equipoRequest recibe un array de equipo request
     * @param transaccion   recibe un array de equipo request
     * @return retorna un equipo response creado
     */
    Equipo crearEquipo(EquipoRequest equipoRequest, Transaccion transaccion);

    /**
     * Método que Lista equipos
     *
     * @param pagina   número de pagina [0..n]
     * @param cantidad cantidad de items por página
     * @return retorna una lista de equipos
     */
    Page<Equipo> listarEquipos(Integer pagina, Integer cantidad);

    /**
     * Método que elimina equipos
     *
     * @param equipoId identificador del equipo
     */
    EliminarResponse eliminarEquipo(String equipoId);

    /**
     * Método para editar información de equipo
     *
     * @param equipoId      identificador del equipo
     * @param equipoRequest recibe un array de equipo request
     * @param transaccion   recibe un array de equipo request
     * @return retorna un equipo response editado
     */
    Equipo editarEquipo(String equipoId, EquipoRequest equipoRequest, Transaccion transaccion);

    /**
     * Método que obtiene el detalle del equipo
     *
     * @param equipoId identificador del equipo
     * @return retorna el equipo response
     */
    EquipoUsuariosResponse getEquipoDetalle(String equipoId);

    /**
     * Metodo que obtiene el equipo por id
     *
     * @param equipoId identificador del equipo
     * @return retorna el equipo
     */
    Equipo obtenerEquipo(String equipoId);

    /**
     * Método que Lista equipos
     *
     * @param searchRequest recibe un array de equipo request
     * @return retorna una lista de equipos
     */
    Page<Equipo> listarSearchEquipo(SearchRequest searchRequest);
}
