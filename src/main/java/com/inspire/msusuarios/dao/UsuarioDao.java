package com.inspire.msusuarios.dao;

import com.inspire.msusuarios.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDao extends JpaRepositoryImplementation<Usuario, String> {

    /**
     * Método que busca un usuario por username
     *
     * @param username nombre de usuario
     * @return retorna un usuario
     */

    boolean existsByUsername(String username);

    /**
     * Método que busca usuarios por estado y ordena por fecha de creación
     *
     * @param estado   estado de usuario
     * @param pageable paginación
     * @return retorna una lista de usuarios
     */
    Page<Usuario> findAllByEstadoOrderByFecCre(Integer estado, Pageable pageable);

    /**
     * Método que busca un usuario por email
     *
     * @param email nombre de usuario
     * @return retorna un usuario
     */
    boolean existsByEmail(String email);

    /**
     * Método que busca un usuario por username
     *
     * @param username nombre de usuario
     * @return retorna un usuario
     */
    Usuario findByUsername(String username);


    /**
     * Metodo que busca un usuario por nombres, apellidos o username
     *
     * @param nombre   Nombres del usuario
     * @param apellido Apellidos del usuario
     * @param username Username del usuario
     * @return Lista de usuarios que cumplen con los criterios de búsqueda
     */
    Page<Usuario> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrUsernameContainingIgnoreCase
    (Pageable pageable, String nombre, String apellido, String username);
}