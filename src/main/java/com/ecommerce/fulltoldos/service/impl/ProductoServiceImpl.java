package com.ecommerce.fulltoldos.service.impl;


import com.ecommerce.fulltoldos.dto.CatalogoFiltro;
import com.ecommerce.fulltoldos.dto.CatalogoResultado;
import com.ecommerce.fulltoldos.model.Producto;
import com.ecommerce.fulltoldos.model.ProductoMedia;
import com.ecommerce.fulltoldos.repository.ProductoMediaRepository;
import com.ecommerce.fulltoldos.repository.ProductoRepository;
import com.ecommerce.fulltoldos.repository.ProductoSpecifications;
import com.ecommerce.fulltoldos.service.FileStorageService;
import com.ecommerce.fulltoldos.service.ProductoCatalogoMapper;
import com.ecommerce.fulltoldos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMediaRepository mediaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProductoCatalogoMapper productoCatalogoMapper;

    @Override
    public Page<Producto> listarTodos(Pageable pageable) {
        return productoRepository.findAllByOrderByFechaCreacionDesc(pageable);
    }

    @Override
    public CatalogoResultado buscarCatalogo(CatalogoFiltro filtro, int page, int size) {
        Specification<Producto> specBase = Specification.where(ProductoSpecifications.activo());
        if (filtro.categoriaId() != null) {
            specBase = specBase.and(ProductoSpecifications.categoria(filtro.categoriaId()));
        }
        if (filtro.search() != null && !filtro.search().isBlank()) {
            specBase = specBase.and(ProductoSpecifications.nombreContiene(filtro.search().trim()));
        }
        if (filtro.medida() != null && !filtro.medida().isBlank()) {
            specBase = specBase.and(ProductoSpecifications.medidaContiene(filtro.medida()));
        }
        if (filtro.onlyOfertas()) {
            specBase = specBase.and(ProductoSpecifications.ofertaVigente());
        }

        Specification<Producto> spec = specBase;
        if (filtro.badges() != null && !filtro.badges().isEmpty()) {
            spec = spec.and(ProductoSpecifications.badgeEnAlguno(filtro.badges()));
        }
        if (filtro.colores() != null && !filtro.colores().isEmpty()) {
            spec = spec.and(ProductoSpecifications.colorEnAlguno(filtro.colores()));
        }

        Sort sort = switch (filtro.sort() == null ? "default" : filtro.sort()) {
            case "name-asc" -> Sort.by("nombre").ascending();
            case "name-desc" -> Sort.by("nombre").descending();
            default -> Sort.by("fechaCreacion").descending();
        };

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);
        Page<Producto> pageResult = productoRepository.findAll(spec, pageable);

        List<Producto> productos = pageResult.getContent();
        List<Long> ids = productos.stream().map(Producto::getId).collect(Collectors.toList());
        if (!ids.isEmpty()) {
            Map<Long, List<ProductoMedia>> mediasPorProducto = mediaRepository
                    .findByProductoIdInOrderByOrdenAsc(ids).stream()
                    .collect(Collectors.groupingBy(m -> m.getProducto().getId()));
            productos.forEach(p -> p.setMedias(mediasPorProducto.getOrDefault(p.getId(), List.of())));
        }

        List<Map<String, Object>> items = productos.stream()
                .map(productoCatalogoMapper::toJson)
                .collect(Collectors.toList());

        return new CatalogoResultado(items, pageResult.getTotalElements(), pageResult.getTotalPages(), calcularFacets(specBase));
    }

    /** Contadores de badges y colores disponibles sobre el conjunto ya filtrado por categoría/búsqueda/medida/oferta. */
    private Map<String, Object> calcularFacets(Specification<Producto> specBase) {
        List<Producto> base = productoRepository.findAll(specBase);

        Map<String, Long> badgeCounts = new LinkedHashMap<>();
        badgeCounts.put("new", 0L);
        badgeCounts.put("hot", 0L);
        badgeCounts.put("sale", 0L);

        Set<String> colores = new LinkedHashSet<>();
        for (Producto p : base) {
            String badge = null;
            if (p.getDescuento() != null && p.getDescuento() > 0) badge = "sale";
            else if (p.isEsDestacado()) badge = "hot";
            else if (p.isEsNuevo()) badge = "new";
            if (badge != null) badgeCounts.merge(badge, 1L, Long::sum);

            if (p.getColores() != null && !p.getColores().isBlank()) {
                Arrays.stream(p.getColores().split(","))
                        .map(String::trim)
                        .filter(c -> c.startsWith("#"))
                        .forEach(colores::add);
            }
        }

        Map<String, Object> facets = new LinkedHashMap<>();
        facets.put("badges", badgeCounts);
        facets.put("colors", colores);
        return facets;
    }

    @Override
    public List<Producto> listarActivos() {
        return productoRepository.findActivosConMedias();
    }

    @Override
    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaConMedias(categoriaId);
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> buscar(String nombre, Long categoriaId) {
        return productoRepository.buscar(nombre, categoriaId);
    }

    @Override
    public Producto guardar(Producto producto) {
        if (producto.getId() != null) {
            Producto existing = productoRepository.findById(producto.getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            existing.setNombre(producto.getNombre());
            existing.setDescripcion(producto.getDescripcion());
            existing.setDescripcionLarga(producto.getDescripcionLarga());
            existing.setPrecio(producto.getPrecio());
            existing.setStock(producto.getStock());
            existing.setMedidas(producto.getMedidas());
            existing.setColores(producto.getColores());
            existing.setCaracteristicas(producto.getCaracteristicas());
            existing.setEspecificaciones(producto.getEspecificaciones());
            existing.setAccesorios(producto.getAccesorios());
            existing.setActivo(producto.isActivo());
            existing.setTecVisible(producto.isTecVisible());
            existing.setEsNuevo(producto.isEsNuevo());
            existing.setEsDestacado(producto.isEsDestacado());
            existing.setDescuento(producto.getDescuento());
            existing.setFechaFinOferta(producto.getFechaFinOferta());
            existing.setCategoria(producto.getCategoria());
            existing.setSubcategoria(producto.getSubcategoria());
            existing.setUnidadMedida(producto.getUnidadMedida());
            return productoRepository.save(existing);
        }
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long id) {
        // Eliminar archivos físicos primero
        List<ProductoMedia> medias = mediaRepository.findByProductoIdOrderByOrdenAsc(id);
        medias.forEach(m -> {
            // Eliminar archivos locales (imágenes y videos subidos); YouTube URLs se ignoran
            String url = m.getUrl();
            if (url != null && url.startsWith("/uploads/")) {
                fileStorageService.eliminarArchivo(url);
            }
        });
        productoRepository.deleteById(id);
    }

    @Override
    public void cambiarEstado(Long id, boolean activo) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setActivo(activo);
            productoRepository.save(p);
        });
    }

    @Override
    public void actualizarTecImagen(Long productoId, String url) {
        productoRepository.findById(productoId).ifPresent(p -> {
            p.setTecImagenUrl(url);
            productoRepository.save(p);
        });
    }

    // ── Media ──

    @Override
    public ProductoMedia agregarImagen(Long productoId, MultipartFile archivo, boolean principal) {
        if (!fileStorageService.esImagenValida(archivo)) {
            throw new RuntimeException("Archivo no válido. Solo se permiten JPG, PNG o WEBP.");
        }

        // Guardar archivo en disco
        String url = fileStorageService.guardarArchivo(archivo, "productos");

        // Si es principal, quitar la anterior
        if (principal) {
            mediaRepository.quitarPrincipal(productoId);
        }

        // Calcular orden (último + 1)
        List<ProductoMedia> existentes = mediaRepository.findByProductoIdOrderByOrdenAsc(productoId);
        int orden = existentes.size();

        // Crear registro en BD
        Producto producto = new Producto();
        producto.setId(productoId);

        ProductoMedia media = new ProductoMedia(producto, "imagen", url, orden, principal);
        return mediaRepository.save(media);
    }

    @Override
    public ProductoMedia agregarVideo(Long productoId, String urlYoutube) {
        List<ProductoMedia> existentes = mediaRepository.findByProductoIdOrderByOrdenAsc(productoId);
        int orden = existentes.size();

        Producto producto = new Producto();
        producto.setId(productoId);

        ProductoMedia media = new ProductoMedia(producto, "video", urlYoutube, orden, false);
        return mediaRepository.save(media);
    }

    @Override
    public ProductoMedia agregarVideoLocal(Long productoId, MultipartFile archivo) {
        if (!fileStorageService.esVideoValido(archivo)) {
            throw new RuntimeException("Archivo no válido. Solo se permiten MP4, WebM, OGG, MOV o AVI.");
        }

        String url = fileStorageService.guardarArchivo(archivo, "videos");

        List<ProductoMedia> existentes = mediaRepository.findByProductoIdOrderByOrdenAsc(productoId);
        int orden = existentes.size();

        Producto producto = new Producto();
        producto.setId(productoId);

        ProductoMedia media = new ProductoMedia(producto, "video", url, orden, false);
        return mediaRepository.save(media);
    }

    @Override
    public void eliminarMedia(Long mediaId) {
        mediaRepository.findById(mediaId).ifPresent(m -> {
            // Si era imagen, eliminar el archivo físico
            if ("imagen".equals(m.getTipo())) {
                fileStorageService.eliminarArchivo(m.getUrl());
            }
            mediaRepository.delete(m);
        });
    }

    @Override
    public void marcarPrincipal(Long mediaId) {
        mediaRepository.findById(mediaId).ifPresent(m -> {
            // Quitar principal del resto
            mediaRepository.quitarPrincipal(m.getProducto().getId());
            // Marcar este como principal
            m.setPrincipal(true);
            mediaRepository.save(m);
        });
    }

    @Override
    public List<ProductoMedia> listarMedia(Long productoId) {
        return mediaRepository.findByProductoIdOrderByOrdenAsc(productoId);
    }
}