package com.inspire.msusuarios.controller;

import com.inspire.mscommon.model.Transaccion;
import com.inspire.mscommon.util.TransaccionUtil;
import com.inspire.msusuarios.dto.request.UsuarioRequest;
import com.inspire.msusuarios.dto.response.UsuariosSgsiResponse;
import com.inspire.msusuarios.mapper.UsuarioSgsiMapper;
import com.inspire.msusuarios.service.contratos.sgsidb.UsersService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Operation(
            summary = "Obtener la lista de usuarios sgsi",
            description = "Recupera una lista de todos los usuarios sgsi disponibles ",
            parameters = {
                    @Parameter(name = "cantidad", description = "Número de registros que se mostrarán por página.", required = true),
                    @Parameter(name = "pagina", description = "Número de la página que se desea consultar.", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "La lista de usuarios sgsi se ha obtenido correctamente."
                    )
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UsuariosSgsiResponse>> getListaUsers(
            @RequestParam(name = "pagina") @Min(0) int pagina,
            @RequestParam(name = "cantidad") @Positive int cantidad) {
        Page<UsuariosSgsiResponse> users = usersService.listarUsers(pagina, cantidad)
                .map(UsuarioSgsiMapper.INSTANCE::usersToUsersResponse);
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint para crear un nuevo users
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
    public ResponseEntity<UsuariosSgsiResponse> crearUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest,
                                                             HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        UsuariosSgsiResponse users = UsuarioSgsiMapper.INSTANCE
                .usersToUsersResponse(usersService.crearUsers(usuarioRequest, transaccion));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Endpoint para editar un usuario existente
     *
     * @param id             ID del usuario a editar
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
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuariosSgsiResponse> editarUsuario(@PathVariable("id") Long id,
                                                              @RequestBody UsuarioRequest usuarioRequest, HttpServletRequest request) {
        Transaccion transaccion = TransaccionUtil.crearTransaccion(request, JwtExtractUserUtil.extractUserDbId());
        UsuariosSgsiResponse usuarioResponse = UsuarioSgsiMapper.INSTANCE
                .usersToUsersResponse(usersService.editarUsers(id, usuarioRequest, transaccion));
        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }
}
