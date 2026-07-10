package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.EnvioAgencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnvioAgenciaRepository extends JpaRepository<EnvioAgencia, Long> {
    List<EnvioAgencia> findByActivoTrueOrderByOrdenAsc();
    List<EnvioAgencia> findAllByOrderByOrdenAsc();
}
