package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.dto.CatalogoFiltro;
import com.ecommerce.fulltoldos.dto.CatalogoResultado;
import com.ecommerce.fulltoldos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/** Búsqueda paginada del catálogo público (grilla de /categoria y /ofertas). */
@RestController
public class CatalogoApiController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/api/catalogo/productos")
    public CatalogoResultado productos(
            @RequestParam(defaultValue = "all") String cat,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "default") String sort,
            @RequestParam(required = false) List<String> badges,
            @RequestParam(required = false) List<String> colors,
            @RequestParam(required = false) String medida,
            @RequestParam(defaultValue = "false") boolean ofertas,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        Long categoriaId = "all".equalsIgnoreCase(cat) ? null : parseLong(cat);

        CatalogoFiltro filtro = new CatalogoFiltro(
                categoriaId,
                q,
                badges != null ? badges : Collections.emptyList(),
                colors != null ? colors : Collections.emptyList(),
                medida,
                ofertas,
                sort
        );

        return productoService.buscarCatalogo(filtro, page, size);
    }

    private Long parseLong(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
