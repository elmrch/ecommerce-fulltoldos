package com.ecommerce.fulltoldos.service;


import com.ecommerce.fulltoldos.model.Producto;
import com.ecommerce.fulltoldos.model.ProductoMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductoService {


    Page<Producto> listarTodos(Pageable pageable);


    List<Producto> listarActivos();


    List<Producto> listarPorCategoria(Long categoriaId);


    Optional<Producto> buscarPorId(Long id);


    List<Producto> buscar(String nombre, Long categoriaId);


    Producto guardar(Producto producto);


    void eliminar(Long id);


    void cambiarEstado(Long id, boolean activo);


    void actualizarTecImagen(Long productoId, String url);


    ProductoMedia agregarImagen(Long productoId, MultipartFile archivo, boolean principal);


    ProductoMedia agregarVideo(Long productoId, String urlYoutube);


    ProductoMedia agregarVideoLocal(Long productoId, MultipartFile archivo);


    void eliminarMedia(Long mediaId);


    void marcarPrincipal(Long mediaId);


    List<ProductoMedia> listarMedia(Long productoId);
}