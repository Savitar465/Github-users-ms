package com.inspire.msusuarios.controller;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.mscommon.util.TransaccionUtil;
import com.inspire.msusuarios.dto.request.UsuarioRequest;
import com.inspire.msusuarios.dto.response.UsuarioResponse;
import com.inspire.msusuarios.mapper.UsuarioMapper;
import com.inspire.msusuarios.service.contratos.UsuarioService;
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
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(value = "v1/usuarios")
@Slf4j
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para obtener la lista de usuarios
     *
     * @param pagina   número de página [0..n]
     * @param cantidad cantidad de items por página
     * @return ResponseEntity que contiene la lista de usuarios y un código de estado HTTP 200
     */
    @Operation(
            summary = "Obtener lista paginada de usuarios",
            description = "Este endpoint permite recuperar una lista paginada de usuarios registrados." +
                    " Es obligatorio proporcionar los parámetros `pagina` y `cantidad` para controlar la paginación.",
            parameters = {
                    @Parameter(name = "cantidad", description = "Número de registros que se mostrarán por página.", required = true),
                    @Parameter(name = "pagina", description = "Número de la página que se desea consultar.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. La lista de usuarios fue obtenida correctamente."
                    )
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UsuarioResponse>> getListaUsuarios(
            @RequestParam(name = "pagina") @Min(0) int pagina,
            @RequestParam(name = "cantidad") @Positive int cantidad) {

        Page<UsuarioResponse> usuarios = usuarioService.listarUsuarios(pagina, cantidad)
                .map(UsuarioMapper.INSTANCE::usuarioToUsuarioResponse);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Endpoint para obtener un usuario por su ID
     *
     * @param usuarioKyId ID del usuario a recuperar
     * @return ResponseEntity que contiene el objeto UsuarioNuevoResponse y un código de estado HTTP 200
     */
    @Operation(
            summary = "Obtener detalles de un usuario específico",
            description = "Este endpoint permite recuperar la información detallada de un usuario registrado." +
                    " Es necesario proporcionar el parámetro `usuarioId`, que identifica de manera única al usuario solicitado.",
            parameters = {
                    @Parameter(name = "usuarioKyId", description = "Identificador único del usuario (UUID)", required = true),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. El usuario fue recuperado exitosamente."
                    )
            }
    )
    @GetMapping(value = "/{usuarioKyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioResponse> getUsuario(@PathVariable("usuarioKyId") String usuarioKyId) {
        UsuarioResponse usuarioResponse = UsuarioMapper.INSTANCE
                .usuarioToUsuarioResponse(usuarioService.getUsuario(usuarioKyId));
        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }

    /**
     * Endpoint para crear un nuevo usuario
     *
     * @param usuarioRequest objeto que contiene los datos del nuevo usuario
     * @return ResponseEntity que contiene el objeto UsuarioNuevoResponse y un código de estado HTTP 201
     */
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Este endpoint permite la creación de un nuevo usuario proporcionando los datos requeridos " +
                    "en el cuerpo de la solicitud. Si la operación es exitosa, devuelve los detalles del usuario creado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un nuevo usuario",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UsuarioRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Operación exitosa. El usuario fue creado exitosamente."
                    )
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest,
                                                        HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        UsuarioResponse usuario = UsuarioMapper.INSTANCE
                .usuarioToUsuarioResponse(usuarioService.crearUsuario(usuarioRequest, transaccion));
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    /**
     * Endpoint para eliminar un usuario por su ID
     *
     * @param usuarioKyId ID del usuario a eliminar
     * @return ResponseEntity que contiene un objeto EliminarResponse y un código de estado HTTP 200
     */
    @Operation(
            summary = "Eliminar un usuario existente",
            description = "Este endpoint permite eliminar permanentemente a un usuario. Es necesario proporcionar el parámetro `usuarioId`, que identifica de manera única al usuario que se desea eliminar.",
            parameters = {
                    @Parameter(name = "usuarioKyId", description = "Identificador del usuario", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. El usuario ha sido eliminado exitosamente."
                    )
            }
    )
    @DeleteMapping(value = "/{usuarioKyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EliminarResponse> eliminarUsuario(@PathVariable("usuarioKyId") String usuarioKyId) {
        return new ResponseEntity<>(usuarioService.eliminarUsuario(usuarioKyId), HttpStatus.OK);
    }

    /**
     * Endpoint para editar un usuario existente
     *
     * @param usuarioKyId    ID del usuario a editar
     * @param usuarioRequest objeto que contiene los datos actualizados del usuario
     * @return ResponseEntity que contiene el objeto UsuarioNuevoResponse editado y un código de estado HTTP 200
     */
    @Operation(
            summary = "Editar un usuario existente",
            description = "Este endpoint permite actualizar los detalles de un usuario registrado. Es necesario proporcionar el parámetro `usuarioId`, que identifica de manera única al usuario que será editado.",
            parameters = {
                    @Parameter(name = "usuarioKyId", description = "Identificador único del usuario", required = true),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para editar un usuario existente",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UsuarioRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. El usuario fue actualizado correctamente."
                    )
            }
    )
    @PutMapping(value = "/{usuarioKyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioResponse> editarUsuario(@PathVariable("usuarioKyId") String usuarioKyId,
                                                         @RequestBody UsuarioRequest usuarioRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        UsuarioResponse usuarioResponse = UsuarioMapper.INSTANCE
                .usuarioToUsuarioResponse(usuarioService.editarUsuario(usuarioKyId, usuarioRequest, transaccion));
        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }

    /**
     * Endpoint para buscar una lista de usuarios por su filtración
     *
     * @param searchRequest filtro de búsqueda
     * @return ResponseEntity que contiene el objeto UsuarioNuevoResponse y un código de estado HTTP 200
     */
    @Operation(
            summary = "Buscar usuarios por filtración",
            description = "Este endpoint permite buscar usuarios por filtración. Es necesario proporcionar el parámetro `filter`," +
                    " que identifica de manera única al usuario que será editado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para buscar usuarios por filtración",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SearchRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. El usuario fue actualizado correctamente."
                    )
            }
    )
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UsuarioResponse>> listarUsuariosSearch(@RequestBody SearchRequest searchRequest) {
        Page<UsuarioResponse> search = usuarioService.listarUsuariosSearch(searchRequest).map(UsuarioMapper.INSTANCE::usuarioToUsuarioResponse);
        return new ResponseEntity<>(search, null, HttpStatus.OK);
    }

}
