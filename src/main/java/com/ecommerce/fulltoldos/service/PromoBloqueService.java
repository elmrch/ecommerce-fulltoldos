package com.ecommerce.fulltoldos.service;

import com.ecommerce.fulltoldos.model.PromoBloque;
import com.ecommerce.fulltoldos.repository.PromoBloqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class PromoBloqueService {

    @Autowired private PromoBloqueRepository repo;
    @Autowired private FileStorageService fileStorage;

    public List<PromoBloque> listarTodos() {
        return repo.findAllByOrderByFilaAscOrdenAsc();
    }

    public LinkedHashMap<Integer, List<PromoBloque>> listarActivosPorFila() {
        List<PromoBloque> bloques = repo.findByActivoTrueOrderByFilaAscOrdenAsc();
        LinkedHashMap<Integer, List<PromoBloque>> map = new LinkedHashMap<>();
        for (PromoBloque b : bloques) {
            map.computeIfAbsent(b.getFila(), k -> new ArrayList<>()).add(b);
        }
        return map;
    }

    public Optional<PromoBloque> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public PromoBloque guardar(PromoBloque bloque, MultipartFile imagen) {
        if (imagen != null && !imagen.isEmpty()) {
            if (!fileStorage.esImagenValida(imagen))
                throw new RuntimeException("Formato de imagen no válido");
            bloque.setImagenUrl(fileStorage.guardarArchivo(imagen, "promos"));
        }
        if (bloque.getId() == null && bloque.getOrden() == 0) {
            int max = repo.findAllByOrderByFilaAscOrdenAsc().stream()
                    .mapToInt(PromoBloque::getOrden).max().orElse(0);
            bloque.setOrden(max + 1);
        }
        return repo.save(bloque);
    }

    public void eliminar(Long id) {
        repo.findById(id).ifPresent(b -> {
            if (b.getImagenUrl() != null) fileStorage.eliminarArchivo(b.getImagenUrl());
            repo.delete(b);
        });
    }

    public void cambiarActivo(Long id, boolean activo) {
        repo.findById(id).ifPresent(b -> { b.setActivo(activo); repo.save(b); });
    }

    public void reordenar(Long id, int dir) {
        List<PromoBloque> lista = repo.findAllByOrderByFilaAscOrdenAsc();
        int idx = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(id)) { idx = i; break; }
        }
        if (idx < 0) return;
        int target = idx + dir;
        if (target < 0 || target >= lista.size()) return;
        PromoBloque a = lista.get(idx), b = lista.get(target);
        int tmp = a.getOrden(); a.setOrden(b.getOrden()); b.setOrden(tmp);
        repo.save(a); repo.save(b);
    }
}