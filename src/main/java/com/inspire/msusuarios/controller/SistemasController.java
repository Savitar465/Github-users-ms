package com.inspire.msusuarios.controller;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.mscommon.util.TransaccionUtil;
import com.inspire.msusuarios.dto.request.SistemaRequest;
import com.inspire.msusuarios.dto.response.SistemaResponse;
import com.inspire.msusuarios.mapper.SistemaMapper;
import com.inspire.msusuarios.service.contratos.SistemaService;
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
@RequestMapping("v1/sistemas")
public class SistemasController {
    private final SistemaService sistemaService;

    public SistemasController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    /**
     * Endpoint para obtener la lista de sistemas
     *
     * @param pagina   número de página a consultar
     * @param cantidad número de registros por página
     * @return ResponseEntity que contiene la lista de sistemas y un código de estado HTTP 200
     */
    @Operation(
            summary = "Obtener la lista de sistemas",
            description = "Recupera una lista de todos los sistemas disponibles en el sistema.",
            parameters = {
                    @Parameter(name = "cantidad", description = "Número de registros que se mostrarán por página.", required = true),
                    @Parameter(name = "pagina", description = "Número de la página que se desea consultar.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La lista de sistemas se ha obtenido correctamente."
                    )
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SistemaResponse>> getListaSistemas(
            @RequestParam(name = "pagina") @Min(0) int pagina,
            @RequestParam(name = "cantidad") @Positive int cantidad) {
        Page<SistemaResponse> sistemas = sistemaService.listarSistemas(pagina, cantidad)
                .map(SistemaMapper.INSTANCE::sistemaToSistemaResponse);
        return ResponseEntity.ok(sistemas);
    }

    /**
     * Endpoint para obtener un sistema específico por su ID
     *
     * @param sistemaId ID del sistema a consultar
     * @return ResponseEntity que contiene el sistema solicitado y un código de estado HTTP 200
     */
    @Operation(
            summary = "Obtención de detalles de un sistema específico",
            description = "Este endpoint permite obtener los detalles de un sistema en particular. " +
                    "Se debe proporcionar el parámetro `sistemaId` para identificar el recurso solicitado.",
            parameters = {
                    @Parameter(name = "sistemaId", description = "Identificador único del sistema (UUID)", required = true),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "El sistema ha sido encontrado exitosamente."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se encontró el sistema con el ID proporcionado."
                    )
            }
    )
    @GetMapping(value = "/{sistemaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SistemaResponse> getSistema(@PathVariable("sistemaId") String sistemaId) {
        SistemaResponse sistema = SistemaMapper.INSTANCE.sistemaToSistemaResponse(sistemaService.obtenerSistema(sistemaId));
        return new ResponseEntity<>(sistema, HttpStatus.OK);
    }

    /**
     * Endpoint para crear un nuevo sistema
     *
     * @param sistemaRequest objeto que contiene la información del nuevo sistema
     * @return ResponseEntity que contiene el sistema creado y un código de estado HTTP 201
     */
    @Operation(
            summary = "Creación de un nuevo sistema",
            description = "Registra un nuevo sistema en la base de datos con los datos proporcionados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un nuevo sistema",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SistemaRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "El sistema ha sido creado exitosamente."
                    )
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SistemaResponse> getcrearSistema(@Valid @RequestBody SistemaRequest sistemaRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        SistemaResponse sistema = SistemaMapper.INSTANCE.sistemaToSistemaResponse(sistemaService.crearSistema(sistemaRequest, transaccion));
        return new ResponseEntity<>(sistema, HttpStatus.CREATED);
    }

    /**
     * Endpoint para editar un sistema existente
     *
     * @param sistemaId      ID del sistema a editar
     * @param sistemaRequest objeto que contiene la información actualizada del sistema
     * @return ResponseEntity que contiene el sistema editado y un código de estado HTTP 202
     */
    @Operation(
            summary = "Editar un sistema existente",
            description = "Recibe un parámetro 'sistemaId' para identificar el sistema y actualizar su información con los datos proporcionados.",
            parameters = {
                    @Parameter(name = "sistemaId", description = "Identificador único del sistema", required = true),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para editar un sistema existente",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SistemaRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "El sistema ha sido actualizado exitosamente."
                    )
            }
    )
    @PutMapping(value = "/{sistemaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SistemaResponse> editarSistema(@PathVariable("sistemaId") String sistemaId,
                                                         @RequestBody SistemaRequest sistemaRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        SistemaResponse sistemaResponse = SistemaMapper.INSTANCE
                .sistemaToSistemaResponse(sistemaService.editarSistema(sistemaId, sistemaRequest, transaccion));
        return new ResponseEntity<>(sistemaResponse, HttpStatus.ACCEPTED);
    }

    /**
     * Endpoint para eliminar un sistema de manera lógica
     *
     * @param sistemaId ID del sistema a eliminar
     * @return ResponseEntity que contiene la respuesta de eliminación y un código de estado HTTP 200
     */
    @Operation(
            summary = "Eliminar un sistema de manera lógica",
            description = "Recibe un parámetro 'sistemaId' para identificar el sistema y proceder con su eliminación lógica en el sistema.",
            parameters = {
                    @Parameter(name = "sistemaId", description = "Identificador del sistema", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "El sistema ha sido eliminado lógicamente de los registros."
                    )
            }
    )
    @DeleteMapping(value = "/{sistemaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EliminarResponse> deleteSistema(@PathVariable("sistemaId") String sistemaId) {
        return new ResponseEntity<>(sistemaService.eliminarSistema(sistemaId), HttpStatus.OK);
    }

    /**
     * Endpoint para buscar una lista de sistemas bajo un filtro
     *
     * @param searchRequest Objeto que contiene los criterios de búsqueda
     * @return ResponseEntity que contiene el objeto sistemaResponse y un código de estado HTTP 200
     */
    @Operation(
            summary = "Buscar sistemas bajo un filtro",
            description = "Este endpoint permite buscar una lista de sistemas bajo un filtro específico." +
                    "Se debe proporcionar el parámetro `searchRequest`, que contiene los criterios de búsqueda.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Criterios de búsqueda para filtrar los sistemas",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SearchRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. Se obtuvo la lista de sistemas filtrados."
                    )
            })
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<SistemaResponse>> searchSistemas(@RequestBody SearchRequest searchRequest) {
        Page<SistemaResponse> cargo = sistemaService.listarSearchSistemas(searchRequest)
                .map(SistemaMapper.INSTANCE::sistemaToSistemaResponse);
        return new ResponseEntity<>(cargo, null, HttpStatus.OK);
    }
}

