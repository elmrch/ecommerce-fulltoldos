package com.ecommerce.fulltoldos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "slides")
@Getter @Setter
public class Slide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int orden = 0;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(length = 300)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "tag_text", length = 100)
    private String tagText;

    @Column(name = "tag_icon", length = 100)
    private String tagIcon;

    @Column(name = "btn_primary_text", length = 100)
    private String btnPrimaryText;

    @Column(name = "btn_primary_icon", length = 100)
    private String btnPrimaryIcon;

    @Column(name = "btn_primary_action", length = 200)
    private String btnPrimaryAction;

    @Column(name = "btn_secondary_text", length = 100)
    private String btnSecondaryText;

    @Column(name = "btn_secondary_icon", length = 100)
    private String btnSecondaryIcon;

    @Column(name = "btn_secondary_href", length = 500)
    private String btnSecondaryHref;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "imagen_link_tipo", length = 50)
    private String imagenLinkTipo;

    @Column(name = "imagen_link_valor", length = 500)
    private String imagenLinkValor;
}
