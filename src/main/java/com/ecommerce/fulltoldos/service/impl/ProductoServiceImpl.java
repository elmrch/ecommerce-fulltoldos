package com.ecommerce.fulltoldos.service.impl;


import com.ecommerce.fulltoldos.model.Producto;
import com.ecommerce.fulltoldos.model.ProductoMedia;
import com.ecommerce.fulltoldos.repository.ProductoMediaRepository;
import com.ecommerce.fulltoldos.repository.ProductoRepository;
import com.ecommerce.fulltoldos.service.FileStorageService;
import com.ecommerce.fulltoldos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMediaRepository mediaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public Page<Producto> listarTodos(Pageable pageable) {
        return productoRepository.findAllByOrderByFechaCreacionDesc(pageable);
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
            existing.setActivo(producto.isActivo());
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