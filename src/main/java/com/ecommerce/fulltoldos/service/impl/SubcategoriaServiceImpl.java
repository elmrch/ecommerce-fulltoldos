package com.ecommerce.fulltoldos.service.impl;

import com.ecommerce.fulltoldos.model.Subcategoria;
import com.ecommerce.fulltoldos.repository.SubcategoriaRepository;
import com.ecommerce.fulltoldos.service.SubcategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubcategoriaServiceImpl implements SubcategoriaService {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Override
    public List<Subcategoria> listarTodas() {
        return subcategoriaRepository.findAllWithCategoria();
    }

    @Override
    public List<Subcategoria> listarActivas() {
        return subcategoriaRepository.findByActivoTrueOrderByOrdenAsc();
    }

    @Override
    public List<Subcategoria> listarPorCategoria(Long categoriaId) {
        return subcategoriaRepository.findByCategoriaIdOrderByOrdenAsc(categoriaId);
    }

    @Override
    public List<Subcategoria> listarActivasPorCategoria(Long categoriaId) {
        return subcategoriaRepository.findByCategoriaIdAndActivoTrueOrderByOrdenAsc(categoriaId);
    }

    @Override
    public Optional<Subcategoria> buscarPorId(Long id) {
        return subcategoriaRepository.findById(id);
    }

    @Override
    public Subcategoria guardar(Subcategoria subcategoria) {
        return subcategoriaRepository.save(subcategoria);
    }

    @Override
    public void eliminar(Long id) {
        subcategoriaRepository.deleteById(id);
    }

    @Override
    public void cambiarEstado(Long id, boolean activo) {
        subcategoriaRepository.findById(id).ifPresent(s -> {
            s.setActivo(activo);
            subcategoriaRepository.save(s);
        });
    }
}