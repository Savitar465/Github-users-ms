package com.inspire.msusuarios.service.contratos;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dto.request.SistemaRequest;
import com.inspire.msusuarios.model.usuarios.Sistema;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;


public interface SistemaService {
    /**
     * Método para obtener todos los sistemas del sistema
     *
     * @param pagina   nro de pagina [0..n]
     * @param cantidad cantidad de items por página
     * @return retorna todos los sistemas
     */
    Page<Sistema> listarSistemas(Integer pagina, Integer cantidad);

    /**
     * Método para crear un sistema
     *
     * @param sistemaRequest recibe un array de sistema request
     * @param transaction    recibe la información de la transacción
     * @return etorna un usuario response creado
     */
    Sistema crearSistema(@Valid SistemaRequest sistemaRequest, Transaccion transaction);

    /**
     * Método para editar un sistema por su id
     *
     * @param sistemaId      recibe ún Id de sistema
     * @param sistemaRequest recibe un array de sistema request
     * @param transaction    recibe la información de la transacción
     * @return retorna una lista con los sistemas asignados
     */
    Sistema editarSistema(String sistemaId, SistemaRequest sistemaRequest, Transaccion transaction);

    /**
     * Método que elimina sistemas
     *
     * @param sistemaId identificador del sistema a eliminar
     */
    EliminarResponse eliminarSistema(String sistemaId);

    /**
     * @param sistemaId recibe ún Id de sistema
     * @return retorna una lista con los sistemas asignados
     */
    Sistema obtenerSistema(String sistemaId);

    /**
     * Metodo
     *
     * @param searchRequest recibe un array de sistemas
     * @return retorna una lista con los sistemas
     */
    Page<Sistema> listarSearchSistemas(SearchRequest searchRequest);
}
