package com.inspire.msusuarios.controller;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.mscommon.util.TransaccionUtil;
import com.inspire.msusuarios.dto.request.ClienteRequest;
import com.inspire.msusuarios.dto.response.ClienteResponse;
import com.inspire.msusuarios.mapper.ClienteMapper;
import com.inspire.msusuarios.service.contratos.ClienteService;
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
@RequestMapping(value = "v1/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Endpoint para obtener la lista de clientes
     *
     * @param pagina   número de página a consultar
     * @param cantidad número de registros por página
     * @return ResponseEntity que contiene la lista de clientes y un código de estado HTTP 200
     */
    @Operation(
            summary = "Obtener la lista de clientes",
            description = "Recupera una lista de todos los clientes disponibles en el sistema.",
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
    public ResponseEntity<Page<ClienteResponse>> getListaClientes(@RequestParam(name = "pagina") @Min(0) int pagina,
                                                                  @RequestParam(name = "cantidad") @Positive int cantidad) {
        Page<ClienteResponse> cliente = clienteService.listarClientes(pagina, cantidad).map(ClienteMapper.INSTANCE::clienteToClienteResponse);
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    /**
     * Endpoint para obtener un cliente específico por su ID
     *
     * @param clienteId ID del cliente a consultar
     * @return ResponseEntity que contiene el rol solicitado y un código de estado HTTP 200
     */
    @Operation(
            summary = "Obtención de detalles de un rol específico",
            description = "Este endpoint permite obtener los detalles de un rol en particular. " +
                    "Se debe proporcionar el parámetro `clienteId` para identificar el recurso solicitado.",
            parameters = {
                    @Parameter(name = "clienteId", description = "Identificador único del rol (UUID)", required = true),
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
    @GetMapping(value = "/{clienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteResponse> getCliente(@PathVariable("clienteId") String clienteId) {
        ClienteResponse cliente = ClienteMapper.INSTANCE.clienteToClienteResponse(clienteService.clienteUnico(clienteId));
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    /**
     * Endpoint para crear un nuevo cliente
     *
     * @param clienteRequest objeto que contiene la información del nuevo cliente
     * @return ResponseEntity que contiene el cliente creado y un código de estado HTTP 201
     */
    @Operation(
            summary = "Creación de un nuevo cliente",
            description = "Registra un nuevo cliente en la base de datos con los datos proporcionados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un nuevo cliente",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRequest.class)
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
    public ResponseEntity<ClienteResponse> crearCliente(@Valid @RequestBody ClienteRequest clienteRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        ClienteResponse cliente = ClienteMapper.INSTANCE.clienteToClienteResponse(clienteService.crearCliente(clienteRequest, transaccion));
        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }

    /**
     * Endpoint para editar un cliente existente
     *
     * @param clienteId      ID del cliente a editar
     * @param clienteRequest objeto que contiene la información actualizada del cliente
     * @return ResponseEntity que contiene el cliente editado y un código de estado HTTP 202
     */
    @Operation(
            summary = "Editar un cliente existente",
            description = "Recibe un parámetro 'clienteId' para identificar el cliente y actualizar su información con los datos proporcionados.",
            parameters = {
                    @Parameter(name = "clienteId", description = "Identificador único del cliente", required = true),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para editar un cliente existente",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "El cliente ha sido actualizado exitosamente."
                    )
            }
    )
    @PutMapping(value = "/{clienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteResponse> editarCliente(@PathVariable("clienteId") String clienteId,
                                                         @RequestBody ClienteRequest clienteRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        ClienteResponse cliente = ClienteMapper.INSTANCE.clienteToClienteResponse(clienteService.editarCliente(clienteId, clienteRequest, transaccion));
        return new ResponseEntity<>(cliente, null, HttpStatus.ACCEPTED);
    }

    /**
     * Endpoint para eliminar un cliente de manera fisica
     *
     * @param clienteId ID del cliente a eliminar
     * @return ResponseEntity que contiene la respuesta de eliminación y un código de estado HTTP 200
     */
    @Operation(
            summary = "Eliminar un cliente de manera fisica",
            description = "Recibe un parámetro 'clienteId' para identificar el cliente y proceder con su eliminación fisica en la base.",
            parameters = {
                    @Parameter(name = "clienteId", description = "Identificador del cliente", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "El cliente ha sido eliminado de los registros."
                    )
            }
    )
    @DeleteMapping(value = "/{clienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EliminarResponse> eliminarCliente(@PathVariable("clienteId") String clienteId) {
        return new ResponseEntity<>(clienteService.eliminarCliente(clienteId), HttpStatus.OK);
    }

    /**
     * Endpoint para buscar una lista de clientes bajo un filtro
     *
     * @param searchRequest Objeto que contiene los criterios de búsqueda
     * @return ResponseEntity que contiene el objeto clienteResponse y un código de estado HTTP 200
     */
    @Operation(
            summary = "Buscar clientes bajo un filtro",
            description = "Este endpoint permite buscar una lista de clientes bajo un filtro específico." +
                    "Se debe proporcionar el parámetro `searchRequest`, que contiene los criterios de búsqueda.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Criterios de búsqueda para filtrar los clientes",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SearchRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. Se obtuvo la lista de clientes filtrados."
                    )
            })
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ClienteResponse>> searchClientes(@RequestBody SearchRequest searchRequest) {
        Page<ClienteResponse> cliente = clienteService.listarSearchClientes(searchRequest)
                .map(ClienteMapper.INSTANCE::clienteToClienteResponse);
        return new ResponseEntity<>(cliente, null, HttpStatus.OK);
    }

}
