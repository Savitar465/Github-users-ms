package com.inspire.msusuarios.dao.usuarios;

import com.inspire.msusuarios.model.usuarios.Equipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipoDao extends JpaRepositoryImplementation<Equipo, String> {

    /**
     * Método que busca un equipo por estado y ordena por fecha de creación y paginación
     *
     * @param estado   estado de equipo
     * @param pageable paginación
     * @return retorna una lista de equipos en paginación
     */
    Page<Equipo> findAllByEstadoOrderByFecCre(Integer estado, Pageable pageable);

    boolean existsByNombre(String username);
}
