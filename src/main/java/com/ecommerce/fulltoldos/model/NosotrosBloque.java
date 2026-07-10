package com.ecommerce.fulltoldos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "nosotros_bloques")
@Getter @Setter
public class NosotrosBloque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(length = 300)
    private String titulo;

    @Column(length = 300)
    private String subtitulo;

    @Column(columnDefinition = "TEXT")
    private String cuerpo;

    @Column(nullable = false)
    private int orden = 0;

    @Column(nullable = false)
    private boolean activo = true;
}
