package com.ecommerce.fulltoldos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "configuracion")
@Getter @Setter
public class Configuracion {

    @Id
    @Column(name = "clave", length = 100)
    private String clave;

    @Column(name = "valor", columnDefinition = "TEXT")
    private String valor;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "grupo", length = 50)
    private String grupo;
}
