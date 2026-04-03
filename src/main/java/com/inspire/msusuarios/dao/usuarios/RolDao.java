package com.inspire.msusuarios.dao.usuarios;

import com.inspire.msusuarios.model.usuarios.RolEquipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface RolDao extends JpaRepositoryImplementation<RolEquipo, String> {
    /**
     * Método que verifica la existencia de un rol por su nombre
     *
     * @param nombre : parámetro nombre con el cuál buscará el rol.
     * @return : retorna un booleano, true si el rol existe, false en caso de no.
     */
    boolean existsByNombre(String nombre);

    /**
     * Método que devuelve el Rol con el nombre igual al del parámetro.
     *
     * @param nombre : parámetro nombre con el cuál buscará el rol.
     * @return : retorna un objeto tipo Rol, null en caso de que no exista.
     */
    RolEquipo findFirstByNombre(String nombre);

    /**
     * Método que busca usuarios por estado y ordena por fecha de creación
     *
     * @param estado   estado de usuario
     * @param pageable paginación
     * @return retorna una lista de usuarios
     */
    Page<RolEquipo> findAllByEstadoOrderByFecCre(Integer estado, Pageable pageable);
}