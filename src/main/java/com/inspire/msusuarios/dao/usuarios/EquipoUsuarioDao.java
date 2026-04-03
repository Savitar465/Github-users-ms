package com.inspire.msusuarios.dao.usuarios;

import com.inspire.msusuarios.model.usuarios.EquipoUsuario;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoUsuarioDao extends JpaRepositoryImplementation<EquipoUsuario, String> {

    /**
     * Método que busca todos los usuarios con su equipo por equipo id
     *
     * @param equipoId identificador de equipo
     * @return retorna una lista de equipo usuario
     */
    List<EquipoUsuario> findAllByEquipoEquipoId(String equipoId);

    /**
     * Método que busca todos los usuarios con su equipo por equipo id y usuario id
     *
     * @param equipoId  identificador de equipo
     * @param usuarioId identificador de usuario
     * @return retorna una lista de equipo usuario
     */
    List<EquipoUsuario> findAllByEquipoEquipoIdAndUsuarioUsuarioKyId(String equipoId, String usuarioId);

    /**
     * Método que busca todos los usuarios con su equipo por tipo de equipo
     *
     * @param tipoEquipo tipo de equipo
     * @return retorna una lista de equipo usuario
     */
    List<EquipoUsuario> findAllByEquipo_TipoAndUsuario_UsuarioKyId(String tipoEquipo, String userIds);

}
