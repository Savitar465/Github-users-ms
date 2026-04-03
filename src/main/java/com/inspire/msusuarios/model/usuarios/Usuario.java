package com.inspire.msusuarios.model.usuarios;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "usuarios")
public class Usuario {
    @Id
    @Column(name = "usuario_ky_id")
    private String usuarioKyId;
    @Column(name = "id_sgsi")
    private Long idSgsi;
    @NotBlank(message = "El email no puede estar vacio")
    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "El email debe ser un correo válido")
    @Column(name = "email")
    private String email;
    @NotBlank(message = "El username no puede estar vacio")
    @NotNull(message = "El username no puede ser nulo")
    @Column(name = "username")
    private String username;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "_estado")
    private Integer estado;
    @Column(name = "usu_cre")
    private String usuCre;
    @Column(name = "fec_cre")
    private Date fecCre;
    @Column(name = "usu_mod")
    private String usuMod;
    @Column(name = "fec_mod")
    private Date fecMod;

    @ManyToOne
    @JoinColumn(name = "cargos_cargo_id")
    private CargoArea cargoArea;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<EquipoUsuario> usuarioEquipo;

}