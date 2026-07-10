package com.ecommerce.fulltoldos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "envio_historial")
@Getter @Setter
public class EnvioHistorial {

    public enum Estado { EN_RUTA, ENTREGADO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(length = 200)
    private String destino;

    @Column(length = 200)
    private String producto;

    @Column(length = 50)
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.EN_RUTA;

    @Column(nullable = false)
    private int orden = 0;

    @Column(nullable = false)
    private boolean activo = true;
}
