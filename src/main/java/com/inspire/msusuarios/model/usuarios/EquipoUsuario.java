package com.inspire.msusuarios.model.usuarios;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "equipos_usuarios")
public class EquipoUsuario {
    @Id
    @Column(name = "equipo_usuario_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String equipoUsuarioId;

    @ManyToOne
    @JoinColumn(name = "roles_rol_id")
    private RolEquipo rol;

    @ManyToOne
    @JoinColumn(name = "usuarios_usuario_ky_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "equipos_equipo_id")
    private Equipo equipo;

}
