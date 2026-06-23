package com.ecommerce.fulltoldos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subcategorias", schema = "public")
public class Subcategoria extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private boolean activo = true;

    @Column
    private int orden = 0;

    public Subcategoria() {}

    public Long getId()                            { return id; }
    public void setId(Long id)                     { this.id = id; }

    public Categoria getCategoria()                { return categoria; }
    public void setCategoria(Categoria c)          { this.categoria = c; }

    public String getNombre()                      { return nombre; }
    public void setNombre(String nombre)           { this.nombre = nombre; }

    public String getDescripcion()                 { return descripcion; }
    public void setDescripcion(String d)           { this.descripcion = d; }

    public boolean isActivo()                      { return activo; }
    public void setActivo(boolean activo)          { this.activo = activo; }

    public int getOrden()                          { return orden; }
    public void setOrden(int orden)                { this.orden = orden; }
}
