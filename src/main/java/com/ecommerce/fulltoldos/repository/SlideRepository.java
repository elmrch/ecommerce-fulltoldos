package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.Slide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlideRepository extends JpaRepository<Slide, Long> {
    List<Slide> findByActivoTrueOrderByOrdenAsc();
    List<Slide> findAllByOrderByOrdenAsc();
}