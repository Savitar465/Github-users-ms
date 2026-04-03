package com.inspire.msusuarios.dao.usuarios;

import com.inspire.msusuarios.model.usuarios.CargoArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoAreaDao extends JpaRepositoryImplementation<CargoArea, String> {
    Page<CargoArea> findAllByEstadoOrderByFecCre(Integer estado, Pageable pageable);
}
