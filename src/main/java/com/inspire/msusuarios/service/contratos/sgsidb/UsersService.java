package com.inspire.msusuarios.service.contratos.sgsidb;

import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dto.request.UsuarioRequest;
import com.inspire.msusuarios.model.sgsidb.UsuariosSgsi;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;


public interface UsersService {
    /**
     * Método para obtener todos los sistemas del sistema
     *
     * @param pagina   nro de pagina [0..n]
     * @param cantidad cantidad de items por página
     * @return retorna todos los sistemas
     */
    Page<UsuariosSgsi> listarUsers(Integer pagina, Integer cantidad);

    /**
     * Método que crea un usuario en el sistema
     *
     * @param usuarioRequest recibe un objeto UsuarioRequest con los datos del usuario
     * @param transaccion    información de la transacción
     * @return retorna un objeto UsuariosSgsi creado
     */
    UsuariosSgsi crearUsers(@Valid UsuarioRequest usuarioRequest, Transaccion transaccion);

    /**
     * Edita un usuarioSgsi  existente.
     *
     * @param id             recibe ún Id de usuario
     * @param usuarioRequest recibe un array de roles que serán añadidos
     * @param transaction    recibe la información de la transacción
     * @return retorna una lista con los usuarios con los roles asignados
     */
    UsuariosSgsi editarUsers(Long id, UsuarioRequest usuarioRequest, Transaccion transaction);
}
