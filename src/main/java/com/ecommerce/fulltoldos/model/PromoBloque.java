package com.ecommerce.fulltoldos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "promo_bloques")
@Getter @Setter
public class PromoBloque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int fila = 1;

    /** Ancho del bloque en porcentaje (1-100). Los bloques de una misma fila deben sumar 100. */
    @Column(name = "flex", nullable = false)
    private int ancho = 100;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "color_base", length = 20)
    private String colorBase = "#1a5276";

    @Column(length = 200)
    private String titulo;

    @Column(length = 300)
    private String descripcion;

    @Column(name = "btn_texto", length = 100)
    private String btnTexto;

    @Column(name = "btn_accion", length = 300)
    private String btnAccion;

    @Column(nullable = false)
    private int orden = 0;

    @Column(nullable = false)
    private boolean activo = true;
}
