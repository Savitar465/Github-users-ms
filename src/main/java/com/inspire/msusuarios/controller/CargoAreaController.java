package com.inspire.msusuarios.controller;

import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.mscommon.util.TransaccionUtil;
import com.inspire.msusuarios.dto.request.CargoAreaRequest;
import com.inspire.msusuarios.dto.response.CargoAreaResponse;
import com.inspire.msusuarios.mapper.CargoAreaMapper;
import com.inspire.msusuarios.service.contratos.CargoAreaService;
import com.inspire.msusuarios.util.JwtExtractUserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/cargo-areas")
public class CargoAreaController {
    private final CargoAreaService cargoAreaService;
    private final CargoAreaMapper cargoAreaMapper;

    public CargoAreaController(CargoAreaService cargoAreaService,
                               CargoAreaMapper cargoAreaMapper) {
        this.cargoAreaService = cargoAreaService;
        this.cargoAreaMapper = cargoAreaMapper;
    }

    /**
     * Lista los cargos paginados.
     *
     * @param pagina   Número de página a consultar.
     * @param cantidad Número de registros por página.
     * @return ResponseEntity con una lista de cargos y un código de estado 200 (OK).
     */
    @Operation(summary = "Lista las Cargos paginadas",
            description = "Devuelve una lista cargos paginadas.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Lista de cargos devuelta correctamente"),
                    @ApiResponse(responseCode = "400",
                            description = "Solicitud inválida")
            }
    )
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<CargoAreaResponse>> listarCargoAreas(@RequestParam(name = "pagina") @Min(0) int pagina,
                                                                    @RequestParam(name = "cantidad") @Positive int cantidad) {
        Page<CargoAreaResponse> cargoAreaResponses = cargoAreaService.listarCargoAreas(pagina, cantidad)
                .map(cargoAreaMapper::cargoAreaToCargoAreaResponse);
        return ResponseEntity.status(HttpStatus.OK).body(cargoAreaResponses);
    }

    /**
     * Obtiene un cargo por su ID.
     *
     * @param cargoId ID del cargo a buscar.
     * @return ResponseEntity con el cargo encontrada o un error 404 si no se encuentra.
     */
    @Operation(summary = "Obtiene un cargo por su ID",
            description = "Devuelve los detalles de un cargo específica basándose en su ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "cargo encontrada y devuelta correctamente"),
                    @ApiResponse(responseCode = "404",
                            description = "cargo no encontrada")
            }
    )
    @GetMapping(value = "/{cargoId}", produces = "application/json")
    public ResponseEntity<CargoAreaResponse> getCargoAreaById(@PathVariable String cargoId) {
        CargoAreaResponse cargoAreaResponse = cargoAreaMapper
                .cargoAreaToCargoAreaResponse(cargoAreaService.getCargoArea(cargoId));
        return ResponseEntity.ok(cargoAreaResponse);
    }

    /**
     * Crea un nuevo cargo.
     *
     * @param cargoAreaRequest Objeto que contiene los detalles del cargo a crear
     * @return ResponseEntity con el cargo creado y un código de estado 201 (CREATED).
     */
    @Operation(summary = "Crea un cargo",
            description = "Crea un nuevo cargo con los detalles proporcionados.",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Cargo creado correctamente"),
                    @ApiResponse(responseCode = "400",
                            description = "Solicitud inválida")
            }
    )
    @PostMapping(value = "/crear", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CargoAreaResponse> crearCargoArea(@Valid @RequestBody CargoAreaRequest cargoAreaRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        CargoAreaResponse cargoAreaResponse = cargoAreaMapper.cargoAreaToCargoAreaResponse(
                cargoAreaService.crearCargoArea(cargoAreaRequest, transaccion));
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoAreaResponse);
    }

    @Operation(summary = "Edita un cargo",
            description = "Actualiza los detalles de un cargo existente.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Cargo editado correctamente"),
                    @ApiResponse(responseCode = "404",
                            description = "Cargo no encontrado"),
                    @ApiResponse(responseCode = "400",
                            description = "Solicitud inválida")
            }
    )
    @PutMapping(value = "/{cargoId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CargoAreaResponse> editarCargoArea(@PathVariable String cargoId,
                                                             @Valid @RequestBody CargoAreaRequest cargoAreaRequest,
                                                             HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        CargoAreaResponse cargoAreaResponse = cargoAreaMapper.cargoAreaToCargoAreaResponse(
                cargoAreaService.editarCargoArea(cargoId, cargoAreaRequest, transaccion));
        return ResponseEntity.ok(cargoAreaResponse);
    }

    /**
     * Elimina un cargo por su ID.
     * @param cargoId ID del cargo a eliminar.
     * @return ResponseEntity con una respuesta de eliminación y un código de estado 200 (OK).
     */
    @Operation(summary = "Elimina un cargo por su ID",
            description = "Elimina un cargo específica basándose en su ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Cargo eliminado correctamente"),
                    @ApiResponse(responseCode = "404",
                            description = "Cargo no encontrado")
            }
    )
    @DeleteMapping(value = "/{cargoId}", produces = "application/json")
    public ResponseEntity<EliminarResponse> eliminarCargoArea(@PathVariable String cargoId) {
        EliminarResponse response = cargoAreaService.eliminarCargoArea(cargoId);
        return ResponseEntity.ok(response);
    }

}
