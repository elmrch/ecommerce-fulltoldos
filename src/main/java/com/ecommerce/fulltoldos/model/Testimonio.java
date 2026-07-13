package com.ecommerce.fulltoldos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "testimonios")
@Getter @Setter
public class Testimonio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 100)
    private String ciudad;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comentario;

    @Column(nullable = false)
    private int estrellas = 5;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(nullable = false)
    private int orden = 0;

    @Column(nullable = false)
    private boolean activo = true;
}
