package com.inspire.msusuarios.service.implementacion;

import com.inspire.mscommon.exceptionhandler.exceptions.EntityConflictException;
import com.inspire.msusuarios.dao.usuarios.EquipoUsuarioDao;
import com.inspire.msusuarios.dto.request.UsuariosCargoRequest;
import com.inspire.msusuarios.model.usuarios.Equipo;
import com.inspire.msusuarios.model.usuarios.EquipoUsuario;
import com.inspire.msusuarios.service.contratos.EquipoUsuarioService;
import com.inspire.msusuarios.service.contratos.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EquipoUsuarioServiceImpl implements EquipoUsuarioService {

    private final EquipoUsuarioDao equipoUsuarioDao;
    private final UsuarioService usuarioService;

    public EquipoUsuarioServiceImpl(EquipoUsuarioDao equipoUsuarioDao,
                                    UsuarioService usuarioService) {
        this.equipoUsuarioDao = equipoUsuarioDao;
        this.usuarioService = usuarioService;
    }

    @Override
    public Page<EquipoUsuario> listarEquiposUsuario(Integer pagina, Integer cantidad) {
        Pageable pageable = PageRequest.of(pagina, cantidad);
        return equipoUsuarioDao.findAll(pageable);
    }

    @Override
    public List<EquipoUsuario> listarUsuariosEquipoPorTipoYUsuariosId(String tipoEquipo, String userId) {
        return equipoUsuarioDao.findAllByEquipo_TipoAndUsuario_UsuarioKyId(tipoEquipo, userId);
    }

    @Override
    public List<EquipoUsuario> guardarListaEquipoUsuarios(List<EquipoUsuario> equipoUsuario) {
        return equipoUsuarioDao.saveAll(equipoUsuario);
    }

    @Override
    public List<EquipoUsuario> construirListaUsuariosPorCargo(UsuariosCargoRequest uc, Equipo equipo) {
        return uc.getUsuariosId().stream().map(id ->
                        EquipoUsuario.builder()
                                .equipo(equipo)
                                .usuario(usuarioService.getUsuario(id))
                                .build()
        ).toList();
    }

    @Override
    public List<EquipoUsuario> asignarUsuarioAEquipo(String equipoId, UsuariosCargoRequest usuariosCargoRequest) {
        List<EquipoUsuario> equipoUsuarios = new ArrayList<>();
        usuariosCargoRequest.getUsuariosId().forEach(userId -> {
            if (equipoUsuarioDao.findAllByEquipoEquipoIdAndUsuarioUsuarioKyId(equipoId, userId).isEmpty()) {
                equipoUsuarios.add(EquipoUsuario.builder()
                                .equipo(Equipo.builder().equipoId(equipoId).build())
                                .usuario(usuarioService.getUsuario(userId))
//                        .cargo(cargoService.getCargo(usuariosCargoRequest.getCargoId()))
                                .build()
                );
            } else {
                throw new EntityConflictException("El usuario con id: " + userId
                        + " ya se encuentra asignado al equipo con id: " + equipoId);
            }
        });
        return equipoUsuarioDao.saveAll(equipoUsuarios);
    }
}
