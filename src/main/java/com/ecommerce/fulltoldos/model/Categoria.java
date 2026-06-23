package com.ecommerce.fulltoldos.model;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categorias", schema = "public")
public class Categoria extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    @Column(nullable = false)
    private boolean activo = true;

    @Column
    private int orden = 0;

    // Un categoria tiene muchos productos
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Producto> productos;

    // ── Constructores ──
    public Categoria() {}

    public Categoria(String nombre, String descripcion) {
        this.nombre      = nombre;
        this.descripcion = descripcion;
    }

    // ── Getters y Setters ──
    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public String getNombre()                  { return nombre; }
    public void setNombre(String nombre)       { this.nombre = nombre; }

    public String getDescripcion()             { return descripcion; }
    public void setDescripcion(String d)       { this.descripcion = d; }

    public String getImagenUrl()               { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public boolean isActivo()                  { return activo; }
    public void setActivo(boolean activo)      { this.activo = activo; }

    public int getOrden()                      { return orden; }
    public void setOrden(int orden)            { this.orden = orden; }

    public List<Producto> getProductos()       { return productos; }
    public void setProductos(List<Producto> p) { this.productos = p; }
}