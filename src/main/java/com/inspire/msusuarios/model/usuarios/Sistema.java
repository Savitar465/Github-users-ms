package com.inspire.msusuarios.model.usuarios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Entity(name = "sistemas")
public class Sistema {
    @Id
    @Column(name = "sistema_id", columnDefinition = "text")
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String sistemaId;
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

    @OneToMany(mappedBy = "sistemasSistemaId",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Cliente> clientes;

}
