package com.ecommerce.fulltoldos.repository;


import com.ecommerce.fulltoldos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Solo categorías activas ordenadas
    List<Categoria> findByActivoTrueOrderByOrdenAsc();

    // Buscar por nombre (para validar duplicados)
    boolean existsByNombre(String nombre);

    // Buscar por nombre ignorando mayúsculas
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}