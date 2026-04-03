package com.inspire.msusuarios.dao.sgsidb;

import com.inspire.msusuarios.model.sgsidb.UsuariosSgsi;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao extends JpaRepositoryImplementation<UsuariosSgsi, Long> {

    /**
     * Método que busca un usuario por username
     *
     * @param username nombre de usuario
     * @return retorna un usuario
     */

    boolean existsByUsername(String username);

}

