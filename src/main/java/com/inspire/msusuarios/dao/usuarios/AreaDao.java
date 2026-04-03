package com.inspire.msusuarios.dao.usuarios;

import com.inspire.msusuarios.model.usuarios.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaDao extends JpaRepositoryImplementation<Area, String> {

    /**
     * Método que busca en la base de datos todas las areas y las devuelve dependiendo del parametro que se pasa
     * por el estado y ordena por fecha de creación
     *
     * @param estado   estado de area
     * @param pageable paginación
     * @return retorna una lista de áreas
     */
    Page<Area> findAllByEstadoOrderByFecCre(Integer estado, Pageable pageable);

    /**
     * Método que comprueba un área existe en la base de datos, verificando lo por nombre del área
     *
     * @param nombre nombre de área
     * @return retorna un área
     */
    boolean existsByNombre(String nombre);
}
