package com.ecommerce.fulltoldos.service;

import com.ecommerce.fulltoldos.model.Slide;
import com.ecommerce.fulltoldos.repository.SlideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class SlideService {

    @Autowired
    private SlideRepository repo;

    @Autowired
    private FileStorageService fileStorage;

    public List<Slide> listarTodos() {
        return repo.findAllByOrderByOrdenAsc();
    }

    public List<Slide> listarActivos() {
        return repo.findByActivoTrueOrderByOrdenAsc();
    }

    public Optional<Slide> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Slide guardar(Slide slide, MultipartFile imagen) {
        if (imagen != null && !imagen.isEmpty()) {
            if (!fileStorage.esImagenValida(imagen)) {
                throw new RuntimeException("Formato de imagen no válido");
            }
            String url = fileStorage.guardarArchivo(imagen, "slides");
            slide.setImagenUrl(url);
        }
        // Asignar orden al final si es nuevo
        if (slide.getId() == null && slide.getOrden() == 0) {
            int maxOrden = repo.findAllByOrderByOrdenAsc().stream()
                    .mapToInt(Slide::getOrden).max().orElse(0);
            slide.setOrden(maxOrden + 1);
        }
        return repo.save(slide);
    }

    public void eliminar(Long id) {
        repo.findById(id).ifPresent(s -> {
            if (s.getImagenUrl() != null) fileStorage.eliminarArchivo(s.getImagenUrl());
            repo.delete(s);
        });
    }

    public void cambiarActivo(Long id, boolean activo) {
        repo.findById(id).ifPresent(s -> { s.setActivo(activo); repo.save(s); });
    }

    public void reordenar(Long id, int direccion) {
        List<Slide> slides = repo.findAllByOrderByOrdenAsc();
        int idx = -1;
        for (int i = 0; i < slides.size(); i++) {
            if (slides.get(i).getId().equals(id)) { idx = i; break; }
        }
        if (idx < 0) return;
        int target = idx + direccion;
        if (target < 0 || target >= slides.size()) return;
        Slide a = slides.get(idx);
        Slide b = slides.get(target);
        int tmp = a.getOrden();
        a.setOrden(b.getOrden());
        b.setOrden(tmp);
        repo.save(a);
        repo.save(b);
    }
}