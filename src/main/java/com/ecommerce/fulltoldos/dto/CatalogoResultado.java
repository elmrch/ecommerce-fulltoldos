package com.ecommerce.fulltoldos.dto;

import java.util.List;
import java.util.Map;

/** Respuesta paginada del catálogo público, con los productos de la página y los facets del sidebar. */
public record CatalogoResultado(
        List<Map<String, Object>> items,
        long total,
        int totalPages,
        Map<String, Object> facets
) {}
