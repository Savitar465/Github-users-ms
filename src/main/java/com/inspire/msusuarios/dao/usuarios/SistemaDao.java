package com.inspire.msusuarios.dao.usuarios;

import com.inspire.msusuarios.model.usuarios.Sistema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface SistemaDao extends JpaRepositoryImplementation<Sistema, String> {
    /**
     * Método que busca un sistema por nombre
     *
     * @param nombre nombre del sistema
     * @return retorna un sistema
     */
    boolean existsByNombre(String nombre);

    /**
     * Metodo que busca en la base de datos todos los sistemas y las devuelve dependiendo del parametro que se pasa
     * por el estado y ordena por fecha de creación
     *
     * @param estado   estado de area
     * @param pageable paginación
     * @return retorna una lista de áreas
     */
    Page<Sistema> findAllByEstadoOrderByFecCre(Integer estado, Pageable pageable);
}
