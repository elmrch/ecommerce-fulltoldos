package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.NosotrosBloque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NosotrosBloqueRepository extends JpaRepository<NosotrosBloque, Long> {
    List<NosotrosBloque> findByActivoTrueOrderByOrdenAsc();
    List<NosotrosBloque> findAllByOrderByOrdenAsc();
}
