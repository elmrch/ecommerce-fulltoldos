package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.PreguntaFrecuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreguntaFrecuenteRepository extends JpaRepository<PreguntaFrecuente, Long> {
    List<PreguntaFrecuente> findByActivoTrueOrderByOrdenAsc();
    List<PreguntaFrecuente> findAllByOrderByOrdenAsc();
}
