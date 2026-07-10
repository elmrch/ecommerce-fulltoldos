package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.EnvioHistorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnvioHistorialRepository extends JpaRepository<EnvioHistorial, Long> {
    List<EnvioHistorial> findByActivoTrueOrderByOrdenAsc();
    List<EnvioHistorial> findAllByOrderByOrdenAsc();
}
