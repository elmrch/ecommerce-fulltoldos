package com.ecommerce.fulltoldos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter @Setter
public class Pedido extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // null si la compra fue como invitado

    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    private String direccion;
    private String referencia;
    private String ciudad;
    private String provincia;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    private String pais = "Perú";

    @Column(name = "metodo_envio")
    private String metodoEnvio;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "costo_envio", precision = 10, scale = 2)
    private BigDecimal costoEnvio = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE_PAGO;

    @Column(name = "culqi_charge_id")
    private String culqiChargeId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> items = new ArrayList<>();

    public enum Estado { PENDIENTE_PAGO, PAGADO, CANCELADO }
}