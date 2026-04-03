package com.inspire.msusuarios.service.contratos;

import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dto.request.CargoAreaRequest;
import com.inspire.msusuarios.model.usuarios.CargoArea;
import org.springframework.data.domain.Page;


public interface CargoAreaService {
    /**
     * Obtiene un CargoArea por su ID.
     *
     * @param cargoId el ID del CargoArea a buscar
     * @return el CargoArea correspondiente al ID proporcionado
     */
    CargoArea getCargoArea(String cargoId);

    /**
     * Crea un nuevo CargoArea.
     *
     * @param cargoArea el CargoAreaRequest a crear
     * @return el CargoArea creado
     */
    CargoArea crearCargoArea(CargoAreaRequest cargoArea, Transaccion transaccion);

    /**
     * Lista los CargoAreas paginados.
     *
     * @param pagina   el número de página a consultar
     * @param cantidad el número de registros por página
     * @return una página de CargoAreas
     */
    Page<CargoArea> listarCargoAreas(int pagina, int cantidad);

    /**
     * Edita un CargoArea existente.
     *
     * @param cargoId          el ID del CargoArea a editar
     * @param cargoAreaRequest el CargoAreaRequest con los nuevos datos
     * @param transaccion      la transacción que contiene información de la modificación
     * @return el CargoArea editado
     */
    CargoArea editarCargoArea(String cargoId, CargoAreaRequest cargoAreaRequest, Transaccion transaccion);

    /**
     * Elimina un CargoArea por su ID.
     *
     * @param cargoId el ID del CargoArea a eliminar
     * @return una respuesta de eliminación que indica el resultado de la operación
     */
    EliminarResponse eliminarCargoArea(String cargoId);
}
