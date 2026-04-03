package com.inspire.msusuarios.controller;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.mscommon.util.TransaccionUtil;
import com.inspire.msusuarios.dto.request.AreaRequest;
import com.inspire.msusuarios.dto.response.AreaResponse;
import com.inspire.msusuarios.mapper.AreaMapper;
import com.inspire.msusuarios.service.contratos.AreaService;
import com.inspire.msusuarios.util.JwtExtractUserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "v1/areas")
public class AreaController {
    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    /**
     * Metodo que lista las áreas por módulo
     *
     * @param cantidad número de pagina [0..n]
     * @param pagina   cantidad de items por página
     * @return retorna una lista de áreas
     */
    @Operation(
            summary = "Obtener lista paginada de áreas",
            description = "Permite recuperar una lista de áreas con soporte para parámetros de paginación, " +
                    "incluyendo el número de página y la cantidad de áreas a mostrar.",
            parameters = {
                    @Parameter(name = "cantidad", description = "Número de registros que se mostrarán por página.", required = true),
                    @Parameter(name = "pagina", description = "Número de la página que se desea consultar.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La lista paginada de áreas se ha recuperado exitosamente."
                    )
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AreaResponse>> listarAreas(@RequestParam(name = "pagina") @Min(0) int pagina,
                                                          @RequestParam(name = "cantidad") @Positive int cantidad) {
        Page<AreaResponse> area = areaService.listarAreas(pagina, cantidad).map(AreaMapper.INSTANCE::areaToAreaResponse);
        return ResponseEntity.ok(area);
    }

    /**
     * Metodo para obtener un área
     *
     * @param areaId identificador de Area
     * @return retorna el área response
     */
    @Operation(
            summary = "Obtener detalles de un área específica",
            description = "Recibe un parámetro 'areaId' para recuperar los detalles de un área, " +
                    "incluyendo información como la lista de usuarios asociados.",
            parameters = {
                    @Parameter(name = "areaId", description = "Identificador único del area (UUID)", required = true),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Los detalles del área se han recuperado exitosamente."
                    )
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{areaId}")
    public ResponseEntity<AreaResponse> getArea(@PathVariable("areaId") String areaId) {
        AreaResponse area = AreaMapper.INSTANCE.areaToAreaResponse(areaService.getArea(areaId));
        return ResponseEntity.ok(area);
    }

    /**
     * Metodo que crea area
     *
     * @param areaRequest recibe un array de area request
     * @return retorna un área response creado
     */
    @Operation(
            summary = "Crear un área nueva",
            description = "Recibe los datos de un área y la registra en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un área",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AreaRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "El área ha sido creada exitosamente en los registros."
                    )
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AreaResponse> crearArea(@Valid @RequestBody AreaRequest areaRequest,
                                                  HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        AreaResponse areaResponse = AreaMapper.INSTANCE.areaToAreaResponse(areaService.crearArea(areaRequest,
                transaccion));
        return new ResponseEntity<>(areaResponse, HttpStatus.CREATED);
    }

    /**
     * Metodo para editar información de área
     *
     * @param areaId      identificador del área
     * @param areaRequest recibe un array de area request
     * @return retorna un área response editado
     */
    @Operation(
            summary = "Actualizar los datos de un área existente",
            description = "Recibe un parámetro 'areaId' para identificar el área y actualizar " +
                    "su información con los datos proporcionados.",
            parameters = {
                    @Parameter(name = "areaId", description = "Identificador único del area", required = true),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para actualizar un área",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AreaRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "El área ha sido actualizada exitosamente en los registros."
                    )
            }
    )
    @PutMapping(value = "/{areaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AreaResponse> editarArea(@PathVariable("areaId") String areaId,
                                                   @RequestBody AreaRequest areaRequest,
                                                   HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        AreaResponse areaResponse = AreaMapper.INSTANCE.areaToAreaResponse(areaService.editarArea(areaId,
                areaRequest, transaccion));
        return new ResponseEntity<>(areaResponse, HttpStatus.ACCEPTED);
    }

    /**
     * Metodo que elimina áreas
     *
     * @param areaId identificador del área
     * @return retorna un área response editado
     */
    @Operation(
            summary = "Eliminar un área de forma lógica",
            description = "Recibe un parámetro 'areaId' para identificar el área y realizar " +
                    "su eliminación lógica del sistema.",
            parameters = {
                    @Parameter(name = "areaId", description = "Identificador del area", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "El área ha sido eliminada lógicamente en los registros."
                    )
            }
    )
    @DeleteMapping(value = "/{areaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EliminarResponse> deleteEquipo(@PathVariable("areaId") String areaId) {
        return new ResponseEntity<>(areaService.eliminarArea(areaId), HttpStatus.OK);
    }

    /**
     * Metodo de busqueda que lista las áreas bajo un filtro
     *
     * @param searchRequest Objeto que contiene los criterios de búsqueda
     * @return retorna una lista de áreas
     */
    @Operation(
            summary = "Busca áreas por un respectivo filtro",
            description = "Recibe un parámetro 'searchRequest' para identificar el área y realizar " +
                    "su eliminación lógica del sistema.",

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Criterios de búsqueda para filtrar áreas",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SearchRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "El área ha sido eliminada lógicamente en los registros."
                    )
            })
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AreaResponse>> listarSearchAreas(@RequestBody SearchRequest searchRequest) {
        Page<AreaResponse> area = areaService.listarSearchAreas(searchRequest)
                .map(AreaMapper.INSTANCE::areaToAreaResponse);
        return new ResponseEntity<>(area, null, HttpStatus.OK);
    }

}
