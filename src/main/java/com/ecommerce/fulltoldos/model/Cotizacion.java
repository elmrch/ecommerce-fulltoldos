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

    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String empresa;

    @Column(name = "tipo_toldo")
    private String tipoToldo;

    private String medidas;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "como_sentero")
    private String comoSentero;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;

    public enum Estado { PENDIENTE, ATENDIDA, CERRADA }
}
