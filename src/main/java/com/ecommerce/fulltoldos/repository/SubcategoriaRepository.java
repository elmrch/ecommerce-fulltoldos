package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface
SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

    @Query("SELECT s FROM Subcategoria s JOIN FETCH s.categoria ORDER BY s.categoria.orden ASC, s.orden ASC")
    List<Subcategoria> findAllWithCategoria();

    List<Subcategoria> findByCategoriaIdOrderByOrdenAsc(Long categoriaId);

    List<Subcategoria> findByCategoriaIdAndActivoTrueOrderByOrdenAsc(Long categoriaId);

    List<Subcategoria> findByActivoTrueOrderByOrdenAsc();

    Optional<Subcategoria> findByNombreAndCategoriaId(String nombre, Long categoriaId);

    boolean existsByNombreAndCategoriaId(String nombre, Long categoriaId);
}