package com.ecommerce.fulltoldos.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {

    @CreatedDate
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @CreatedBy
    @Column(name = "usuario_creacion", length = 150, updatable = false)
    private String usuarioCreacion;

    @LastModifiedBy
    @Column(name = "usuario_modificacion", length = 150)
    private String usuarioModificacion;

    public LocalDateTime getFechaCreacion()               { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime f)         { this.fechaCreacion = f; }

    public LocalDateTime getFechaModificacion()           { return fechaModificacion; }
    public void setFechaModificacion(LocalDateTime f)     { this.fechaModificacion = f; }

    public String getUsuarioCreacion()                    { return usuarioCreacion; }
    public void setUsuarioCreacion(String u)              { this.usuarioCreacion = u; }

    public String getUsuarioModificacion()                { return usuarioModificacion; }
    public void setUsuarioModificacion(String u)          { this.usuarioModificacion = u; }
}
