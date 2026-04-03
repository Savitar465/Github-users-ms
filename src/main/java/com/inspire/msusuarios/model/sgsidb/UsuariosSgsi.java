package com.inspire.msusuarios.model.sgsidb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "pruebas_sgsi")
public class UsuariosSgsi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidos")
    private String apellidos;
    @NotBlank(message = "El email no puede estar vacio")
    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "El email debe ser un correo válido")
    @Column(name = "email")
    private String email;
    @NotBlank(message = "El username no puede estar vacio")
    @NotNull(message = "El username no puede ser nulo")
    @Column(name = "username")
    private String username;
    @Column(name = "estado")
    private Integer estado;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
}
