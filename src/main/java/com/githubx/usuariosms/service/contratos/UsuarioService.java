package com.githubx.usuariosms.service.contratos;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.githubx.usuariosms.dto.request.UsuarioRequest;
import com.githubx.usuariosms.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;


public interface UsuarioService {

    /**
     * Método que lista los usuarios
     *
     * @param pagina   número de pagina [0..n]
     * @param cantidad cantidad de items por página
     * @return retorna una lista de usuarios
     */
    Page<Usuario> listarUsuarios(Integer pagina, Integer cantidad);

    /**
     * Método para obtener un usuario
     *
     * @param usuarioKyId identificador de usuario
     * @return retorna el usuario response
     */
    Usuario getUsuario(String usuarioKyId);

    /**
     * Método que crea un usuario
     *
     * @param usuarioRequest recibe un array de usuario request
     * @param transaction    recibe la información de la transacción
     * @return etorna un usuario response creado
     */
    Usuario crearUsuario(@Valid UsuarioRequest usuarioRequest, Transaccion transaction);

    /**
     * Método que elimina un usuario por su id
     *
     * @param usuarioKyId id del usuario a eliminar
     */
    EliminarResponse eliminarUsuario(String usuarioKyId);

    /**
     * Edita un CargoArea existente.
     *
     * @param usuarioKyId    recibe ún Id de usuario
     * @param usuarioRequest recibe un array de roles que serán añadidos
     * @param transaction    recibe la información de la transacción
     * @return retorna una lista con los usuarios con los roles asignados
     */
    Usuario editarUsuario(String usuarioKyId, UsuarioRequest usuarioRequest, Transaccion transaction);


    /**
     * Metodo que lista mediante un filtro los usuarios
     *
     * @param searchRequest recibe un array de atributos que serán añadidos
     * @return retorna una lista con los usuarios filtrados
     */
    Page<Usuario> listarUsuariosSearch(SearchRequest searchRequest);
}