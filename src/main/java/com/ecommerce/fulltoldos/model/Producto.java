package com.ecommerce.fulltoldos.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "productos", schema = "public")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchos productos pertenecen a una categoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(precision = 10, scale = 2)
    private BigDecimal precio = BigDecimal.ZERO;

    @Column
    private int stock = 0;

    @Column(length = 255)
    private String medidas;

    @Column(length = 255)
    private String colores;

    @Column(columnDefinition = "TEXT")
    private String caracteristicas;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion = LocalDate.now();

    // Un producto tiene muchos archivos media
    @OneToMany(mappedBy = "producto",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<ProductoMedia> medias;

    // ── Constructores ──
    public Producto() {}

    public Producto(String nombre, String descripcion, Categoria categoria) {
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.categoria   = categoria;
    }

    // ── Helper: imagen principal ──
    public String getImagenPrincipal() {
        if (medias == null || medias.isEmpty()) return null;
        return medias.stream()
                .filter(m -> "imagen".equals(m.getTipo()) && m.isPrincipal())
                .map(ProductoMedia::getUrl)
                .findFirst()
                .orElse(medias.stream()
                        .filter(m -> "imagen".equals(m.getTipo()))
                        .map(ProductoMedia::getUrl)
                        .findFirst()
                        .orElse(null));
    }

    // ── Getters y Setters ──
    public Long getId()                            { return id; }
    public void setId(Long id)                     { this.id = id; }

    public Categoria getCategoria()                { return categoria; }
    public void setCategoria(Categoria c)          { this.categoria = c; }

    public String getNombre()                      { return nombre; }
    public void setNombre(String nombre)           { this.nombre = nombre; }

    public String getDescripcion()                 { return descripcion; }
    public void setDescripcion(String d)           { this.descripcion = d; }

    public BigDecimal getPrecio()                  { return precio; }
    public void setPrecio(BigDecimal precio)       { this.precio = precio; }

    public int getStock()                          { return stock; }
    public void setStock(int stock)                { this.stock = stock; }

    public String getMedidas()                     { return medidas; }
    public void setMedidas(String medidas)         { this.medidas = medidas; }

    public String getColores()                     { return colores; }
    public void setColores(String colores)         { this.colores = colores; }

    public String getCaracteristicas()             { return caracteristicas; }
    public void setCaracteristicas(String c)       { this.caracteristicas = c; }

    public boolean isActivo()                      { return activo; }
    public void setActivo(boolean activo)          { this.activo = activo; }

    public LocalDate getFechaCreacion()            { return fechaCreacion; }
    public void setFechaCreacion(LocalDate f)      { this.fechaCreacion = f; }

    public List<ProductoMedia> getMedias()         { return medias; }
    public void setMedias(List<ProductoMedia> m)   { this.medias = m; }
}