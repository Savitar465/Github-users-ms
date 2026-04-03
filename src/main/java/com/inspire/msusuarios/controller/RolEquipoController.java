package com.inspire.msusuarios.controller;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.mscommon.util.TransaccionUtil;
import com.inspire.msusuarios.dto.request.RolRequest;
import com.inspire.msusuarios.dto.response.RolResponse;
import com.inspire.msusuarios.mapper.RolEquipoMapper;
import com.inspire.msusuarios.service.contratos.RolService;
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
@RequestMapping(value = "v1/roles")
public class RolEquipoController {
    private final RolService rolService;

    public RolEquipoController(RolService rolService) {
        this.rolService = rolService;
    }

    /**
     * Endpoint para obtener la lista de roles
     *
     * @param pagina   número de página a consultar
     * @param cantidad número de registros por página
     * @return ResponseEntity que contiene la lista de roles y un código de estado HTTP 200
     */
    @Operation(
            summary = "Obtener la lista de roles",
            description = "Recupera una lista de todos los roles disponibles en el sistema.",
            parameters = {
                    @Parameter(name = "cantidad", description = "Número de registros que se mostrarán por página.", required = true),
                    @Parameter(name = "pagina", description = "Número de la página que se desea consultar.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La lista de roles se ha obtenido correctamente."
                    )
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<RolResponse>> getListaRoles(@RequestParam(name = "pagina") @Min(0) int pagina,
                                                           @RequestParam(name = "cantidad") @Positive int cantidad) {
        Page<RolResponse> roles = rolService.listarRoles(pagina, cantidad).map(RolEquipoMapper.INSTANCE::rolToRolResponse);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    /**
     * Endpoint para obtener un rol específico por su ID
     *
     * @param rolId ID del rol a consultar
     * @return ResponseEntity que contiene el rol solicitado y un código de estado HTTP 200
     */
    @Operation(
            summary = "Obtención de detalles de un rol específico",
            description = "Este endpoint permite obtener los detalles de un rol en particular. " +
                    "Se debe proporcionar el parámetro `rolId` para identificar el recurso solicitado.",
            parameters = {
                    @Parameter(name = "rolId", description = "Identificador único del rol (UUID)", required = true),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "El rol ha sido encontrado exitosamente."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se encontró el rol con el ID proporcionado."
                    )
            }
    )
    @GetMapping(value = "/{rolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RolResponse> getRol(@PathVariable("rolId") String rolId) {
        RolResponse rolResponse = RolEquipoMapper.INSTANCE.rolToRolResponse(rolService.getRol(rolId));
        return new ResponseEntity<>(rolResponse, HttpStatus.OK);
    }

    /**
     * Endpoint para crear un nuevo rol
     *
     * @param rolRequest objeto que contiene la información del nuevo rol
     * @return ResponseEntity que contiene el rol creado y un código de estado HTTP 201
     */
    @Operation(
            summary = "Creación de un nuevo rol",
            description = "Registra un nuevo rol en la base de datos con los datos proporcionados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un nuevo rol",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RolRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "El rol ha sido creado exitosamente."
                    )
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RolResponse> crearRol(@Valid @RequestBody RolRequest rolRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        RolResponse rol = RolEquipoMapper.INSTANCE.rolToRolResponse(rolService.crearRol(rolRequest, transaccion));
        return new ResponseEntity<>(rol, HttpStatus.CREATED);
    }

    /**
     * Endpoint para editar un rol existente
     *
     * @param rolId      ID del rol a editar
     * @param rolRequest objeto que contiene la información actualizada del rol
     * @return ResponseEntity que contiene el rol editado y un código de estado HTTP 202
     */
    @Operation(
            summary = "Editar un rol existente",
            description = "Recibe un parámetro 'rolId' para identificar el rol y actualizar su información con los datos proporcionados.",
            parameters = {
                    @Parameter(name = "rolId", description = "Identificador único del rol", required = true),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para editar un rol existente",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RolRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "El rol ha sido actualizado exitosamente."
                    )
            }
    )
    @PutMapping(value = "/{rolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RolResponse> editarRol(@PathVariable("rolId") String rolId,
                                                 @RequestBody RolRequest rolRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        RolResponse rolResponse = RolEquipoMapper.INSTANCE.rolToRolResponse(rolService.editarRol(rolId, rolRequest, transaccion));
        return new ResponseEntity<>(rolResponse, null, HttpStatus.ACCEPTED);
    }

    /**
     * Endpoint para eliminar un rol de manera fisica
     *
     * @param rolId ID del rol a eliminar
     * @return ResponseEntity que contiene la respuesta de eliminación y un código de estado HTTP 200
     */
    @Operation(
            summary = "Eliminar un rol de manera fisica",
            description = "Recibe un parámetro 'rolId' para identificar el rol y proceder con su eliminación fisica en la base.",
            parameters = {
                    @Parameter(name = "rolId", description = "Identificador del rol", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "El rol ha sido eliminado de los registros."
                    )
            }
    )
    @DeleteMapping(value = "/{rolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EliminarResponse> eliminarRol(@PathVariable("rolId") String rolId) {
        return new ResponseEntity<>(rolService.eliminarRol(rolId), HttpStatus.OK);
    }

    /**
     * Endpoint para buscar una lista de roles bajo un filtro
     *
     * @param searchRequest Objeto que contiene los criterios de búsqueda
     * @return ResponseEntity que contiene el objeto rolResponse y un código de estado HTTP 200
     */
    @Operation(
            summary = "Buscar roles bajo un filtro",
            description = "Este endpoint permite buscar una lista de roles bajo un filtro específico." +
                    "Se debe proporcionar el parámetro `searchRequest`, que contiene los criterios de búsqueda.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Criterios de búsqueda para filtrar los roles",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SearchRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. Se obtuvo la lista de roles filtrados."
                    )
            })
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<RolResponse>> searchRoles(@RequestBody SearchRequest searchRequest) {
        Page<RolResponse> rol = rolService.listarSearchRoles(searchRequest)
                .map(RolEquipoMapper.INSTANCE::rolToRolResponse);
        return new ResponseEntity<>(rol, null, HttpStatus.OK);
    }
}