package com.ecommerce.fulltoldos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cotizaciones")
@Getter @Setter
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Datos del cliente ──
    private String nombre;
    private String correo;
    private String celular;
    private String empresa;

    // ── Producto ──
    @Column(name = "tipo_producto")
    private String tipoProducto;

    // ── Medidas ──
    private String ancho;
    private String largo;
    private String altura;

    // ── Imágenes adjuntas (URLs separadas por coma) ──
    @Column(columnDefinition = "TEXT")
    private String imagenes;

    // ── Comentarios / descripción del proyecto ──
    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;

    public enum Estado { PENDIENTE, ATENDIDA, CERRADA }

    /** Texto legible de las medidas, sin mostrar "null" cuando faltan datos. */
    public String getMedidasTexto() {
        StringBuilder sb = new StringBuilder();
        if (ancho != null && !ancho.isBlank()) sb.append(ancho).append("m ancho");
        if (largo != null && !largo.isBlank()) sb.append(sb.length() > 0 ? " x " : "").append(largo).append("m largo");
        if (altura != null && !altura.isBlank()) sb.append(sb.length() > 0 ? " · " : "").append("alto ").append(altura).append("m");
        return sb.length() > 0 ? sb.toString() : "Sin especificar";
    }

    /** URLs de imágenes adjuntas como lista (vacía si no hay). */
    public java.util.List<String> getListaImagenes() {
        if (imagenes == null || imagenes.isBlank()) return java.util.List.of();
        return java.util.Arrays.stream(imagenes.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();
    }
}
