package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.Testimonio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestimonioRepository extends JpaRepository<Testimonio, Long> {
    List<Testimonio> findByActivoTrueOrderByOrdenAsc();
    List<Testimonio> findAllByOrderByOrdenAsc();
}
