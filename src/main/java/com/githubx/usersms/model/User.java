package com.githubx.usersms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Entity(name = "usuarios")
public class User {
    @Id
    @Column(name = "usuario_ky_id")
    private String userId;
    @NotBlank(message = "Email cannot be empty")
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email must be a valid email address")
    @Column(name = "email")
    private String email;
    @NotBlank(message = "Username cannot be empty")
    @NotNull(message = "Username cannot be null")
    @Column(name = "username")
    private String username;
    @Column(name = "nombres")
    private String firstName;
    @Column(name = "apellidos")
    private String lastName;
    @Column(name = "_estado")
    private Integer status;
    @Column(name = "usu_cre")
    private String createdByUser;
    @Column(name = "fec_cre")
    private Date createdDate;
    @Column(name = "usu_mod")
    private String modifiedByUser;
    @Column(name = "fec_mod")
    private Date modifiedDate;

}