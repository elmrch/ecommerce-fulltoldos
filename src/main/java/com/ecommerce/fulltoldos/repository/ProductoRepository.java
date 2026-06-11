package com.ecommerce.fulltoldos.repository;


import com.ecommerce.fulltoldos.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Todos los productos activos (con medias en una sola query — evita N+1)
    @Query("SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.medias WHERE p.activo = true ORDER BY p.fechaCreacion DESC")
    List<Producto> findActivosConMedias();

    // Todos los productos activos (sin medias, para compatibilidad)
    List<Producto> findByActivoTrueOrderByFechaCreacionDesc();

    // Productos por categoría (con medias en una sola query)
    @Query("SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.medias WHERE p.activo = true AND p.categoria.id = :categoriaId")
    List<Producto> findByCategoriaConMedias(@Param("categoriaId") Long categoriaId);

    // Productos por categoría
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    // Búsqueda por nombre (barra de búsqueda)
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    // Búsqueda combinada: nombre + categoría (con medias para evitar N+1)
    @Query("SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.medias WHERE " +
            "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%',:nombre,'%'))) AND " +
            "(:categoriaId IS NULL OR p.categoria.id = :categoriaId) AND " +
            "p.activo = true")
    List<Producto> buscar(@Param("nombre") String nombre,
                          @Param("categoriaId") Long categoriaId);

    // Con paginación para el admin
    Page<Producto> findAllByOrderByFechaCreacionDesc(Pageable pageable);

    // Contar productos por categoría
    long countByCategoriaId(Long categoriaId);
}