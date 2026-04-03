package com.inspire.msusuarios.controller;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.mscommon.util.TransaccionUtil;
import com.inspire.msusuarios.dto.request.AreaRequest;
import com.inspire.msusuarios.dto.request.EquipoRequest;
import com.inspire.msusuarios.dto.response.EquipoResponse;
import com.inspire.msusuarios.dto.response.EquipoUsuariosResponse;
import com.inspire.msusuarios.mapper.EquipoMapper;
import com.inspire.msusuarios.service.contratos.EquipoService;
import com.inspire.msusuarios.util.JwtExtractUserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "v1/equipos")
public class EquipoController {
    private final EquipoService equipoService;
    private final EquipoMapper equipoMapper;

    @Autowired
    public EquipoController(EquipoService equipoService, EquipoMapper equipoMapper) {
        this.equipoService = equipoService;
        this.equipoMapper = equipoMapper;
    }

    /**
     * Metodo para crear un equipo
     *
     * @param equipoRequest recibe un array de equipo request
     * @return retorna un equipo response creado
     */
    @Operation(summary = "Crear un equipo nueva", description = "Recibe los datos de un equipo y la registra en el sistema.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para crear un equipo", required = true, content = @Content(schema = @Schema(implementation = EquipoRequest.class))), responses = {@ApiResponse(responseCode = "201", description = "El equipo ha sido creada exitosamente en los registros.")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EquipoResponse> crearEquipo(@Valid @RequestBody EquipoRequest equipoRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        EquipoResponse equipoResponse = EquipoMapper.INSTANCE.equipoToEquipoResponse(equipoService.crearEquipo(equipoRequest, transaccion));
        return new ResponseEntity<>(equipoResponse, HttpStatus.CREATED);
    }

    /**
     * Metodo que lista los equipos por módulo
     *
     * @param cantidad número de pagina [0..n]
     * @param pagina   cantidad de items por página
     * @return retorna una lista de equipos
     */
    @Operation(summary = "Obtener lista paginada de equipos", description = "Permite recuperar una lista de equipos con soporte para parámetros de paginación, " + "incluyendo el número de página y la cantidad de equipos a mostrar.", parameters = {@Parameter(name = "cantidad", description = "Número de registros que se mostrarán por página.", required = true), @Parameter(name = "pagina", description = "Número de la página que se desea consultar.", required = true)}, responses = {@ApiResponse(responseCode = "200", description = "La lista paginada de equipos se ha recuperado exitosamente.")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<EquipoResponse>> getListaEquipos(@RequestParam Integer pagina, @RequestParam Integer cantidad) {
        Page<EquipoResponse> equipoResponses = equipoService.listarEquipos(pagina, cantidad).map(EquipoMapper.INSTANCE::equipoToEquipoResponse);
        return new ResponseEntity<>(equipoResponses, HttpStatus.OK);
    }

    /**
     * Metodo que elimina un equipo
     *
     * @param equipoId identificador del equipo
     * @return retorna un equipo response editado
     */
    @Operation(summary = "Eliminar un equipo de forma lógica", description = "Recibe un parámetro 'equipoId' para identificar el equipo y realizar " + "su eliminación lógica del sistema.", parameters = {@Parameter(name = "equipoId", description = "Identificador del equipo", required = true)}, responses = {@ApiResponse(responseCode = "200", description = "El equipo ha sido eliminada lógicamente en los registros.")})
    @DeleteMapping(value = "/{equipoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EliminarResponse> deleteEquipo(@PathVariable("equipoId") String equipoId) {
        return new ResponseEntity<>(equipoService.eliminarEquipo(equipoId), HttpStatus.OK);
    }

    /**
     * Metodo para editar la información de un equipo
     *
     * @param equipoId      identificador del equipo
     * @param equipoRequest recibe un array de equipo request
     * @return retorna un equipo response editado
     */
    @Operation(summary = "Actualizar los datos de un equipo existente", description = "Recibe un parámetro 'equipoId' para identificar el equipo y actualizar " + "su información con los datos proporcionados.", parameters = {@Parameter(name = "equipoId", description = "Identificador único del equipo", required = true),}, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para actualizar un equipo", required = true, content = @Content(schema = @Schema(implementation = AreaRequest.class))), responses = {@ApiResponse(responseCode = "202", description = "El equipo ha sido actualizada exitosamente en los registros.")})
    @PutMapping(value = "/{equipoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EquipoResponse> editarEquipo(@PathVariable("equipoId") String equipoId, @RequestBody EquipoRequest equipoRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        EquipoResponse equipoResponse = equipoMapper.equipoToEquipoResponse(equipoService.editarEquipo(equipoId, equipoRequest, transaccion));
        return new ResponseEntity<>(equipoResponse, null, HttpStatus.ACCEPTED);
    }

    /**
     * Metodo para obtener un equipo en específico
     *
     * @param equipoId identificador de un equipo
     * @return retorna el equipo response
     */
    @Operation(summary = "Obtener detalles de un equipo específico", description = "Recibe un parámetro 'equipoId' para recuperar los detalles de un equipo, " + "incluyendo información como la lista de usuarios asociados.", parameters = {@Parameter(name = "equipoId", description = "Identificador único del equipo (UUID)", required = true),}, responses = {@ApiResponse(responseCode = "200", description = "Los detalles del equipo se han recuperado exitosamente.")})
    @GetMapping(value = "/{equipoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EquipoUsuariosResponse> getEquipo(@PathVariable("equipoId") String equipoId) {
        EquipoUsuariosResponse equipo = equipoService.getEquipoDetalle(equipoId);
        return new ResponseEntity<>(equipo, null, HttpStatus.OK);
    }

    /**
     * Metodo de busqueda que lista los equipos bajo un filtro
     *
     * @param searchRequest Objeto que contiene los criterios de búsqueda
     * @return retorna una lista de equipos
     */
    @Operation(summary = "Busca equipos por un respectivo filtro", description = "Recibe un parámetro 'searchRequest' para identificar el equipo y realizar " + "su eliminación lógica del sistema.",

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Criterios de búsqueda para filtrar equipos", required = true, content = @Content(schema = @Schema(implementation = SearchRequest.class))), responses = {@ApiResponse(responseCode = "200", description = "El equipo ha sido eliminada lógicamente en los registros.")})
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<EquipoResponse>> listarSearchEquipo(@RequestBody SearchRequest searchRequest) {
        Page<EquipoResponse> equipo = equipoService.listarSearchEquipo(searchRequest).map(EquipoMapper.INSTANCE::equipoToEquipoResponse);
        return new ResponseEntity<>(equipo, null, HttpStatus.OK);
    }

}
