package com.ecommerce.fulltoldos.service;

import com.ecommerce.fulltoldos.model.Producto;
import com.ecommerce.fulltoldos.model.ProductoMedia;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Convierte un {@link Producto} al formato JSON que consume el catálogo público (scrip.js). */
@Component
public class ProductoCatalogoMapper {

    public Map<String, Object> toJson(Producto p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getId());
        m.put("name", p.getNombre());
        m.put("cat", String.valueOf(p.getCategoria().getId()));
        m.put("catName", p.getCategoria().getNombre());
        String desc = p.getDescripcion() != null ? p.getDescripcion() : "";
        m.put("desc", desc);
        m.put("longDesc", desc);
        m.put("descLarga", p.getDescripcionLarga() != null && !p.getDescripcionLarga().isBlank()
            ? p.getDescripcionLarga() : desc);
        m.put("icon", "bi-box-seam");
        m.put("esNuevo",      p.isEsNuevo());
        m.put("esDestacado",  p.isEsDestacado());
        m.put("descuento",    p.getDescuento() != null ? p.getDescuento() : 0);
        m.put("stock",        p.getStock());
        m.put("precioOferta", p.getPrecioConDescuento());
        m.put("fechaFinOferta", p.getFechaFinOferta() != null
            ? p.getFechaFinOferta().toString() : null);
        String badge = null;
        if (p.getDescuento() != null && p.getDescuento() > 0) badge = "sale";
        else if (p.isEsDestacado()) badge = "hot";
        else if (p.isEsNuevo()) badge = "new";
        m.put("badge", badge);
        List<String> features = Collections.emptyList();
        if (p.getCaracteristicas() != null && !p.getCaracteristicas().trim().isEmpty()) {
            features = Arrays.stream(p.getCaracteristicas().split("[,\n]"))
                .map(String::trim).filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        }
        m.put("features", features);
        List<String> medidas = new ArrayList<>();
        Map<String, BigDecimal> medidaPrecios = new LinkedHashMap<>();
        if (p.getMedidas() != null && !p.getMedidas().trim().isEmpty()) {
            String rawMedidas = p.getMedidas();
            String[] lineasMedidas = rawMedidas.contains("\n")
                ? rawMedidas.split("\n") : rawMedidas.split(",");
            for (String linea : lineasMedidas) {
                String t = linea.trim();
                if (t.isEmpty()) continue;
                int idx = t.indexOf(':');
                String nombreMedida = idx > -1 ? t.substring(0, idx).trim() : t;
                if (nombreMedida.isEmpty()) continue;
                medidas.add(nombreMedida);
                if (idx > -1) {
                    String precioStr = t.substring(idx + 1).trim()
                        .replaceAll("[^0-9.]", "");
                    if (!precioStr.isEmpty()) {
                        try {
                            medidaPrecios.put(nombreMedida, new BigDecimal(precioStr));
                        } catch (NumberFormatException ignored) { }
                    }
                }
            }
        }
        m.put("medidas", medidas);
        m.put("medidaPrecios", medidaPrecios);
        List<String> colores = new ArrayList<>();
        if (p.getColores() != null && !p.getColores().trim().isEmpty()) {
            colores = Arrays.stream(p.getColores().split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        }
        m.put("colores", colores);
        // Principal primero, luego el resto en orden
        List<String> imgs = new ArrayList<>();
        if (p.getMedias() != null) {
            p.getMedias().stream()
                .filter(media -> "imagen".equals(media.getTipo()) && media.isPrincipal())
                .map(ProductoMedia::getUrl)
                .forEach(imgs::add);
            p.getMedias().stream()
                .filter(media -> "imagen".equals(media.getTipo()) && !media.isPrincipal())
                .map(ProductoMedia::getUrl)
                .forEach(imgs::add);
        }
        m.put("imgs", imgs);
        List<String> videos = new ArrayList<>();
        if (p.getMedias() != null) {
            p.getMedias().stream()
                .filter(media -> "video".equals(media.getTipo()))
                .map(ProductoMedia::getUrl)
                .forEach(videos::add);
        }
        m.put("videos", videos);
        m.put("precio", p.getPrecio());
        m.put("rating", (p.isRatingVisible() && p.getRating() != null && p.getRating() > 0) ? p.getRating() : null);
        m.put("reviews", 0);
        m.put("subcat", p.getSubcategoria() != null ? String.valueOf(p.getSubcategoria().getId()) : null);
        m.put("subcatName", p.getSubcategoria() != null ? p.getSubcategoria().getNombre() : null);
        m.put("unidad", p.getUnidadMedida() != null ? p.getUnidadMedida() : "UNIDAD");
        m.put("tecImagen", p.getTecImagenUrl());
        m.put("tecVisible", p.isTecVisible());
        List<Map<String, String>> especificaciones = new ArrayList<>();
        if (p.getEspecificaciones() != null && !p.getEspecificaciones().trim().isEmpty()) {
            Arrays.stream(p.getEspecificaciones().split("\n"))
                .map(String::trim).filter(s -> !s.isEmpty())
                .forEach(linea -> {
                    int idx = linea.indexOf(':');
                    Map<String, String> fila = new LinkedHashMap<>();
                    fila.put("label", idx > -1 ? linea.substring(0, idx).trim() : linea);
                    fila.put("value", idx > -1 ? linea.substring(idx + 1).trim() : "");
                    especificaciones.add(fila);
                });
        }
        m.put("especificaciones", especificaciones);
        List<String> accesorios = Collections.emptyList();
        if (p.getAccesorios() != null && !p.getAccesorios().trim().isEmpty()) {
            accesorios = Arrays.stream(p.getAccesorios().split("[,\n]"))
                .map(String::trim).filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        }
        m.put("accesorios", accesorios);
        return m;
    }
}
