package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.PromoBloque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PromoBloqueRepository extends JpaRepository<PromoBloque, Long> {
    List<PromoBloque> findAllByOrderByFilaAscOrdenAsc();
    List<PromoBloque> findByActivoTrueOrderByFilaAscOrdenAsc();
    List<PromoBloque> findByFila(int fila);
}
