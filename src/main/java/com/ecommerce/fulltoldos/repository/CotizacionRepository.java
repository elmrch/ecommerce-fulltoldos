package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    List<Cotizacion> findAllByOrderByFechaSolicitudDesc();
    List<Cotizacion> findByEstado(Cotizacion.Estado estado);
    List<Cotizacion> findByCorreoIgnoreCaseOrderByFechaSolicitudDesc(String correo);
}
