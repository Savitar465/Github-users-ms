package com.inspire.msusuarios.service.implementacion;

import com.inspire.mscommon.abstracts.EstadoAbstract;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.exceptionhandler.exceptions.EntityNotFoundException;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dao.usuarios.CargoAreaDao;
import com.inspire.msusuarios.dto.request.CargoAreaRequest;
import com.inspire.msusuarios.model.usuarios.CargoArea;
import com.inspire.msusuarios.service.contratos.AreaService;
import com.inspire.msusuarios.service.contratos.CargoAreaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CargoAreaServiceImpl implements CargoAreaService {

    private final CargoAreaDao cargoAreaDao;
    private final AreaService areaService;

    public CargoAreaServiceImpl(CargoAreaDao cargoAreaDao, AreaService areaService) {
        this.cargoAreaDao = cargoAreaDao;
        this.areaService = areaService;
    }

    @Override
    public CargoArea getCargoArea(String cargoId) {
        return cargoAreaDao.findById(cargoId).orElseThrow(() ->
                new EntityNotFoundException("No existe CargoArea con el id: " + cargoId)
        );
    }

    @Override
    public CargoArea crearCargoArea(CargoAreaRequest cargoArea, Transaccion transaccion) {
        return cargoAreaDao.save(
                CargoArea.builder()
                        .nombre(cargoArea.getNombre())
                        .descripcion(cargoArea.getDescripcion())
                        .estado(EstadoAbstract.ACTIVO)
                        .areasAreaId(areaService.getArea(cargoArea.getAreasAreaId()))
                        .usuCre(transaccion.getTrUsuarioId())
                        .fecCre(transaccion.getTrFecha())
                        .usuMod(transaccion.getTrUsuarioId())
                        .fecMod(transaccion.getTrFecha())
                        .build()
        );
    }

    @Override
    public Page<CargoArea> listarCargoAreas(int pagina, int cantidad) {
        return cargoAreaDao.findAllByEstadoOrderByFecCre(
                EstadoAbstract.ACTIVO,
                PageRequest.of(pagina, cantidad)
        );
    }

    @Override
    public CargoArea editarCargoArea(String cargoId, CargoAreaRequest cargoAreaRequest, Transaccion transaccion) {
        CargoArea cargoArea = cargoAreaDao.findById(cargoId).orElseThrow(() ->
                new EntityNotFoundException("No existe CargoArea con el id: " + cargoId)
        );
        cargoArea.setNombre(cargoAreaRequest.getNombre());
        cargoArea.setDescripcion(cargoAreaRequest.getDescripcion());
        cargoArea.setAreasAreaId(areaService.getArea(cargoAreaRequest.getAreasAreaId()));
        cargoArea.setUsuMod(transaccion.getTrUsuarioId());
        cargoArea.setFecMod(transaccion.getTrFecha());
        return cargoAreaDao.save(cargoArea);
    }

    @Override
    public EliminarResponse eliminarCargoArea(String cargoId) {
        CargoArea cargoArea = cargoAreaDao.findById(cargoId).orElseThrow(() ->
                new EntityNotFoundException("No existe CargoArea con el id: " + cargoId)
        );
        cargoArea.setEstado(EstadoAbstract.INACTIVO);
        cargoAreaDao.save(cargoArea);
        return new EliminarResponse(cargoId,"CargoArea eliminado correctamente");
    }
}
