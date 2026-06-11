package com.ecommerce.fulltoldos.service;


import com.ecommerce.fulltoldos.model.Producto;
import com.ecommerce.fulltoldos.model.ProductoMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    // Listar todos (admin)
    Page<Producto> listarTodos(Pageable pageable);

    // Listar activos (tienda)
    List<Producto> listarActivos();

    // Por categoría
    List<Producto> listarPorCategoria(Long categoriaId);

    // Buscar por id
    Optional<Producto> buscarPorId(Long id);

    // Buscar (barra de búsqueda)
    List<Producto> buscar(String nombre, Long categoriaId);

    // Guardar (crear y editar)
    Producto guardar(Producto producto);

    // Eliminar
    void eliminar(Long id);

    // Cambiar estado activo
    void cambiarEstado(Long id, boolean activo);

    // ── Media ──

    // Agregar imagen al producto
    ProductoMedia agregarImagen(Long productoId, MultipartFile archivo, boolean principal);

    // Agregar video (URL de YouTube)
    ProductoMedia agregarVideo(Long productoId, String urlYoutube);

    // Agregar video local (archivo subido)
    ProductoMedia agregarVideoLocal(Long productoId, MultipartFile archivo);

    // Eliminar un media
    void eliminarMedia(Long mediaId);

    // Marcar imagen como principal
    void marcarPrincipal(Long mediaId);

    // Listar media de un producto
    List<ProductoMedia> listarMedia(Long productoId);
}