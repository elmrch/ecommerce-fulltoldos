package com.ecommerce.fulltoldos.dto;

import java.util.List;

/** Filtros de búsqueda para el catálogo público paginado (GET /api/catalogo/productos). */
public record CatalogoFiltro(
        Long categoriaId,
        String search,
        List<String> badges,
        List<String> colores,
        String medida,
        boolean onlyOfertas,
        String sort
) {}
