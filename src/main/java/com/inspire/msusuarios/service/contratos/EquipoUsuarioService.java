package com.inspire.msusuarios.service.contratos;

import com.inspire.msusuarios.dto.request.UsuariosCargoRequest;
import com.inspire.msusuarios.model.usuarios.Equipo;
import com.inspire.msusuarios.model.usuarios.EquipoUsuario;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EquipoUsuarioService {

    /**
     * Método que lista usuarios de un equipo por tipo
     *
     * @param tipoEquipo tipo de equipo
     * @return retorna una lista de equipo usuario
     */
    List<EquipoUsuario> listarUsuariosEquipoPorTipoYUsuariosId(String tipoEquipo, String userId);

    /**
     * Método que guarda una lista de equipo usuario
     *
     * @param equipoUsuario lista de equipo usuario
     * @return retorna una lista de equipo usuario guardado
     */
    List<EquipoUsuario> guardarListaEquipoUsuarios(List<EquipoUsuario> equipoUsuario);

    /**
     * Método que ocnstruye Lista de usuarios por Cargo
     *
     * @param uc     recibe un array de usuario cargo request
     * @param equipo {@link Equipo} recibe un equipo para asignarle los usuarios
     * @return retorna una lista de equipos de usuarios por cargo
     */
    List<EquipoUsuario> construirListaUsuariosPorCargo(UsuariosCargoRequest uc, Equipo equipo);

    /**
     * Método que asigna una lista de usuarios a un equipo
     *
     * @param equipoId             identificador del equipo
     * @param usuariosCargoRequest lista de usuarios a asignar con cargo
     * @return retorna una lista de equipo usuario
     */
    List<EquipoUsuario> asignarUsuarioAEquipo(String equipoId, UsuariosCargoRequest usuariosCargoRequest);

    /**
     * Método que lista equipos
     *
     * @param pagina   número de pagina [0..n]
     * @param cantidad cantidad de items por página
     * @return retorna una lista de equipos
     */
    Page<EquipoUsuario> listarEquiposUsuario(Integer pagina, Integer cantidad);
}
