package com.ecommerce.fulltoldos.repository;


import com.ecommerce.fulltoldos.model.ProductoMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoMediaRepository extends JpaRepository<ProductoMedia, Long> {

    // Todos los media de un producto ordenados
    List<ProductoMedia> findByProductoIdOrderByOrdenAsc(Long productoId);

    // Solo imágenes de un producto
    List<ProductoMedia> findByProductoIdAndTipoOrderByOrdenAsc(Long productoId, String tipo);

    // Imagen principal de un producto
    Optional<ProductoMedia> findByProductoIdAndPrincipalTrue(Long productoId);

    // Quitar principal anterior antes de poner uno nuevo
    @Modifying
    @Transactional
    @Query("UPDATE ProductoMedia m SET m.principal = false WHERE m.producto.id = :productoId")
    void quitarPrincipal(@Param("productoId") Long productoId);

    // Eliminar todos los media de un producto
    @Modifying
    @Transactional
    void deleteByProductoId(Long productoId);
}