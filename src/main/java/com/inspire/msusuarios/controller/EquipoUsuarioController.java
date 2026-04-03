package com.inspire.msusuarios.controller;

import com.inspire.msusuarios.dto.request.UsuariosCargoRequest;
import com.inspire.msusuarios.dto.response.EquipoUsuarioResponse;
import com.inspire.msusuarios.mapper.EquipoUsuarioMapper;
import com.inspire.msusuarios.service.contratos.EquipoUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;

@RestController
@RequestMapping(value = "v1/equipos-usuarios")
public class EquipoUsuarioController {
    private final EquipoUsuarioService equipoUsuarioService;

    public EquipoUsuarioController(EquipoUsuarioService equipoUsuarioService) {
        this.equipoUsuarioService = equipoUsuarioService;
    }

    @Operation(summary = "Obtiene la lista de equiposUsuarios", description = "Recibe parámetro de paginacion y cantidad de equipos a mostrar y devuelve la lista de equiposUsuarios")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<EquipoUsuarioResponse>> getListaEquiposUsuarios(@RequestParam(name = "pagina") @Min(0) int pagina, @RequestParam(name = "cantidad") @Positive int cantidad) {
        Page<EquipoUsuarioResponse> equipoUsuario = equipoUsuarioService.listarEquiposUsuario(pagina, cantidad).map(EquipoUsuarioMapper.INSTANCE::equipoUsuarioToEquipoUsuarioResponse);
        return ResponseEntity.ok(equipoUsuario);
    }

    @Operation(summary = "Agregar usuarios a un equipo", description = "Recibe paramatro equipoId y la lista de usuarios para agregarlos al equipo")
    @PostMapping(value = "/{equipoId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EquipoUsuarioResponse>> agregarUsuarioAEquipo(@RequestBody UsuariosCargoRequest usuariosCargoRequest, @PathVariable("equipoId") String equipoId) {
        List<EquipoUsuarioResponse> equipoUsuarios = equipoUsuarioService.asignarUsuarioAEquipo(equipoId, usuariosCargoRequest).stream().map(EquipoUsuarioMapper.INSTANCE::equipoUsuarioToEquipoUsuarioResponse).toList();
        return new ResponseEntity<>(equipoUsuarios, null, HttpStatus.CREATED);
    }


    @Operation(summary = "Obtiene la lista de usuarios de un equipo por tipo", description = "Recibe parametro tipoEquipo para obtener el la lista de usuarios del equipo")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EquipoUsuarioResponse>> getListaUsuariosEquipoPorTipoYUsuarios(@RequestParam("tipoEquipo") String tipoEquipo, @RequestParam("userId") String userId) {
        List<EquipoUsuarioResponse> usuariosEquipo = equipoUsuarioService.listarUsuariosEquipoPorTipoYUsuariosId(tipoEquipo, userId).stream().map(EquipoUsuarioMapper.INSTANCE::equipoUsuarioToEquipoUsuarioResponse).toList();
        return new ResponseEntity<>(usuariosEquipo, null, HttpStatus.OK);
    }
}
