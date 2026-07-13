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

    /** Cómo encajar la imagen en el bloque: "cover" (recorta y llena) o "contain" (se ve completa). */
    @Column(name = "imagen_ajuste", length = 20)
    private String imagenAjuste = "cover";

    /** Zoom de la imagen en porcentaje (50-200). 100 = tamaño normal. */
    @Column(name = "imagen_zoom")
    private Integer imagenZoom = 100;

    /** Posición horizontal del recorte/encuadre (0=izquierda, 50=centro, 100=derecha). */
    @Column(name = "imagen_pos_x")
    private Integer imagenPosX = 50;

    /** Posición vertical del recorte/encuadre (0=arriba, 50=centro, 100=abajo). */
    @Column(name = "imagen_pos_y")
    private Integer imagenPosY = 50;

    /** Alto del bloque en px. Null = usa el alto por defecto de la fila (300px fila 1, 220px las demás). */
    @Column(name = "altura_px")
    private Integer alturaPx;

    /** Si es false, no se muestra el tinte de color sobre la imagen. */
    @Column(name = "overlay_visible")
    private Boolean overlayVisible = true;

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

    public int getAlturaEfectiva() {
        if (alturaPx != null && alturaPx > 0) return alturaPx;
        return fila == 1 ? 300 : 220;
    }
}
