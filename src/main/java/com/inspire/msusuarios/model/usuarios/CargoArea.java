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

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "cargos_areas")
public class CargoArea {
    @Id
    @Column(name = "cargo_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cargoId;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
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
    @JoinColumn(name = "areas_area_id")
    private Area areasAreaId;

}
