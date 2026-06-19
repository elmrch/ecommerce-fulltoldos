package com.ecommerce.fulltoldos.service;

import com.ecommerce.fulltoldos.model.Subcategoria;

import java.util.List;
import java.util.Optional;

public interface SubcategoriaService {

    List<Subcategoria> listarTodas();

    List<Subcategoria> listarActivas();

    List<Subcategoria> listarPorCategoria(Long categoriaId);

    List<Subcategoria> listarActivasPorCategoria(Long categoriaId);

    Optional<Subcategoria> buscarPorId(Long id);

    Subcategoria guardar(Subcategoria subcategoria);

    void eliminar(Long id);

    void cambiarEstado(Long id, boolean activo);
}