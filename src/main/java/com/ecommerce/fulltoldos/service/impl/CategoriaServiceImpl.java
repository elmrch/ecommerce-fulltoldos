package com.ecommerce.fulltoldos.service.impl;
import com.ecommerce.fulltoldos.model.Categoria;
import com.ecommerce.fulltoldos.repository.CategoriaRepository;
import com.ecommerce.fulltoldos.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    public List<Categoria> listarActivas() {
        return categoriaRepository.findByActivoTrueOrderByOrdenAsc();
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    public void cambiarEstado(Long id, boolean activo) {
        categoriaRepository.findById(id).ifPresent(c -> {
            c.setActivo(activo);
            categoriaRepository.save(c);
        });
    }

    @Override
    public boolean existeNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }
}