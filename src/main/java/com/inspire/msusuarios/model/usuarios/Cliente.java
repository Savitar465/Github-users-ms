package com.inspire.msusuarios.model.usuarios;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "clientes")

public class Cliente {
    @Id
    @Column(name = "cliente_id")
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String clienteId;
    @Column(name = "cliente_name")
    private String clienteName;
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
    @JoinColumn(name = "sistemas_sistema_id")
    private Sistema sistemasSistemaId;
    @Column(name = "tipo")
    private String tipo;

    @Type(JsonBinaryType.class)
    @Column(name = "config", columnDefinition = "jsonb")
    private Map<String, Object> config;


}
