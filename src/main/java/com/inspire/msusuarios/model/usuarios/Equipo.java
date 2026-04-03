package com.inspire.msusuarios.model.usuarios;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Entity(name = "equipos")
public class Equipo {
    @Id
    @Column(name = "equipo_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String equipoId;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "tipo")
    private String tipo;
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
    @Column(name = "path_id")
    private String pathId;

    @ManyToOne
    @JoinColumn(name = "equipos_equipo_id")
    private Equipo equipoPadre;
    @JsonIgnore
    @OneToMany(mappedBy = "equipoPadre")
    private List<Equipo> equipoHijos;

    @OneToMany(mappedBy = "equipo",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EquipoUsuario> equipos;
}
