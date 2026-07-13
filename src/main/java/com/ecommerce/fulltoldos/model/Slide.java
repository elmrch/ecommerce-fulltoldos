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

    @Column(name = "color_titulo", length = 20)
    private String colorTitulo;

    @Column(name = "fuente_titulo", length = 60)
    private String fuenteTitulo;

    @Column(name = "tamano_titulo", length = 60)
    private String tamanoTitulo;

    @Column(name = "color_descripcion", length = 20)
    private String colorDescripcion;

    @Column(name = "fuente_descripcion", length = 60)
    private String fuenteDescripcion;

    @Column(name = "tamano_descripcion", length = 60)
    private String tamanoDescripcion;

    @Column(name = "color_btn_primario", length = 20)
    private String colorBtnPrimario;

    @Column(name = "color_btn_secundario", length = 20)
    private String colorBtnSecundario;

    @Column(nullable = false)
    private boolean activo = true;

    /** Si es false, no se muestra el bloque de texto (título, descripción, tag, botones) sobre la imagen. */
    @Column(name = "mostrar_texto", nullable = false)
    private boolean mostrarTexto = true;

    @Column(name = "imagen_link_tipo", length = 50)
    private String imagenLinkTipo;

    @Column(name = "imagen_link_valor", length = 500)
    private String imagenLinkValor;
}
