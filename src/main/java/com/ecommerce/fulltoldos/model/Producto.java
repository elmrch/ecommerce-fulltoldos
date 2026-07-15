package com.ecommerce.fulltoldos.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "productos", schema = "public")
public class Producto extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategoria_id")
    private Subcategoria subcategoria;

    @Column(name = "unidad_medida", length = 50)
    private String unidadMedida;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "descripcion_larga", columnDefinition = "TEXT")
    private String descripcionLarga;

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

    @Column(columnDefinition = "TEXT")
    private String especificaciones;

    @Column(columnDefinition = "TEXT")
    private String accesorios;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "es_nuevo")
    private boolean esNuevo = false;

    @Column(name = "es_destacado")
    private boolean esDestacado = false;

    @Column(name = "descuento")
    private Integer descuento = 0;

    @Column(name = "fecha_fin_oferta")
    private LocalDateTime fechaFinOferta;

    @Column(name = "tec_imagen_url", length = 500)
    private String tecImagenUrl;

    @Column(name = "tec_visible", nullable = false)
    private boolean tecVisible = false;

    @Column(name = "rating")
    private Integer rating = 0;

    @Column(name = "rating_visible", nullable = false, columnDefinition = "boolean default false")
    private boolean ratingVisible = false;

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

    public Subcategoria getSubcategoria()          { return subcategoria; }
    public void setSubcategoria(Subcategoria s)    { this.subcategoria = s; }

    public String getUnidadMedida()                { return unidadMedida; }
    public void setUnidadMedida(String u)          { this.unidadMedida = u; }

    public String getNombre()                      { return nombre; }
    public void setNombre(String nombre)           { this.nombre = nombre; }

    public String getDescripcion()                 { return descripcion; }
    public void setDescripcion(String d)           { this.descripcion = d; }

    public String getDescripcionLarga()            { return descripcionLarga; }
    public void setDescripcionLarga(String d)      { this.descripcionLarga = d; }

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

    public String getEspecificaciones()            { return especificaciones; }
    public void setEspecificaciones(String e)      { this.especificaciones = e; }

    public String getAccesorios()                  { return accesorios; }
    public void setAccesorios(String a)            { this.accesorios = a; }

    public boolean isActivo()                      { return activo; }
    public void setActivo(boolean activo)          { this.activo = activo; }

    public boolean isEsNuevo()                     { return esNuevo; }
    public void setEsNuevo(boolean esNuevo)        { this.esNuevo = esNuevo; }

    public boolean isEsDestacado()                 { return esDestacado; }
    public void setEsDestacado(boolean v)          { this.esDestacado = v; }

    public Integer getDescuento()                  { return descuento; }
    public void setDescuento(Integer descuento)    { this.descuento = descuento; }

    public LocalDateTime getFechaFinOferta()       { return fechaFinOferta; }
    public void setFechaFinOferta(LocalDateTime f) { this.fechaFinOferta = f; }

    public String getTecImagenUrl()                { return tecImagenUrl; }
    public void setTecImagenUrl(String url)         { this.tecImagenUrl = url; }

    public boolean isTecVisible()                  { return tecVisible; }
    public void setTecVisible(boolean tecVisible)  { this.tecVisible = tecVisible; }

    public Integer getRating()                     { return rating; }
    public void setRating(Integer rating)          { this.rating = rating; }

    public boolean isRatingVisible()                    { return ratingVisible; }
    public void setRatingVisible(boolean ratingVisible) { this.ratingVisible = ratingVisible; }

    public BigDecimal getPrecioConDescuento() {
        if (precio == null || precio.compareTo(BigDecimal.ZERO) == 0) return null;
        if (descuento == null || descuento <= 0) return null;
        return precio.multiply(BigDecimal.valueOf(100 - descuento))
                     .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public boolean isAgotado()                     { return stock == 0; }

    public List<ProductoMedia> getMedias()         { return medias; }
    public void setMedias(List<ProductoMedia> m)   { this.medias = m; }
}