package com.inspire.msusuarios.dao.usuarios;


import com.inspire.msusuarios.model.usuarios.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteDao extends JpaRepositoryImplementation<Cliente, String> {
    /**
     * Metodo que verifica la existencia de un Cliente por su nombre
     *
     * @param nombre : parámetro nombre con el cual buscará el cliente.
     * @return : retorna un booleano, true si el rol existe, false en caso de no.
     */
    boolean existsByClienteName(String nombre);

    /**
     * Metodo que busca en la base de datos todos los clientes y las devuelve dependiendo del parámetro que se pasa
     * por el estado y ordena por fecha de creación
     *
     * @param estado   estado de area
     * @param pageable paginación
     * @return retorna una lista de áreas
     */
    Page<Cliente> findAllByEstado(Integer estado, Pageable pageable);
}
