package com.ecommerce.fulltoldos.model;


import jakarta.persistence.*;

@Entity
@Table(name = "producto_media", schema = "public")
public class ProductoMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchos media pertenecen a un producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // "imagen" o "video"
    @Column(nullable = false, length = 10)
    private String tipo;

    // Ruta del archivo o URL de YouTube
    @Column(nullable = false, length = 500)
    private String url;

    @Column
    private int orden = 0;

    // Si es true, es la imagen que aparece en la tarjeta del producto
    @Column
    private boolean principal = false;

    // ── Constructores ──
    public ProductoMedia() {}

    public ProductoMedia(Producto producto, String tipo, String url, int orden, boolean principal) {
        this.producto  = producto;
        this.tipo      = tipo;
        this.url       = url;
        this.orden     = orden;
        this.principal = principal;
    }

    // ── Helper: saber si es YouTube ──
    public boolean isYoutube() {
        return url != null && (url.contains("youtube.com") || url.contains("youtu.be"));
    }

    // ── Getters y Setters ──
    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public Producto getProducto()              { return producto; }
    public void setProducto(Producto p)        { this.producto = p; }

    public String getTipo()                    { return tipo; }
    public void setTipo(String tipo)           { this.tipo = tipo; }

    public String getUrl()                     { return url; }
    public void setUrl(String url)             { this.url = url; }

    public int getOrden()                      { return orden; }
    public void setOrden(int orden)            { this.orden = orden; }

    public boolean isPrincipal()               { return principal; }
    public void setPrincipal(boolean principal){ this.principal = principal; }
}