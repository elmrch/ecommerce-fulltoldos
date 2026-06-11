package com.ecommerce.fulltoldos.service;


import com.ecommerce.fulltoldos.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    // Listar todas
    List<Categoria> listarTodas();

    // Solo activas (para la tienda)
    List<Categoria> listarActivas();

    // Buscar por id
    Optional<Categoria> buscarPorId(Long id);

    // Guardar (crear y editar)
    Categoria guardar(Categoria categoria);

    // Eliminar
    void eliminar(Long id);

    // Activar / desactivar
    void cambiarEstado(Long id, boolean activo);

    // Verificar si el nombre ya existe
    boolean existeNombre(String nombre);
}