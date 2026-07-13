package com.ecommerce.fulltoldos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "envio_agencias")
@Getter @Setter
public class EnvioAgencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(length = 500)
    private String url;

    @Column(nullable = false)
    private int orden = 0;

    @Column(nullable = false)
    private boolean activo = true;
}
