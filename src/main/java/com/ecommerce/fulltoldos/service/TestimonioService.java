package com.ecommerce.fulltoldos.service;

import com.ecommerce.fulltoldos.model.Testimonio;
import com.ecommerce.fulltoldos.repository.TestimonioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class TestimonioService {

    @Autowired
    private TestimonioRepository repo;

    @Autowired
    private FileStorageService fileStorage;

    public List<Testimonio> listarTodos() {
        return repo.findAllByOrderByOrdenAsc();
    }

    public List<Testimonio> listarActivos() {
        return repo.findByActivoTrueOrderByOrdenAsc();
    }

    public Optional<Testimonio> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Testimonio guardar(Testimonio testimonio, MultipartFile foto) {
        if (foto != null && !foto.isEmpty()) {
            if (!fileStorage.esImagenValida(foto)) {
                throw new RuntimeException("Formato de imagen no válido");
            }
            String url = fileStorage.guardarArchivo(foto, "testimonios");
            testimonio.setFotoUrl(url);
        }
        if (testimonio.getId() == null && testimonio.getOrden() == 0) {
            int maxOrden = repo.findAllByOrderByOrdenAsc().stream()
                    .mapToInt(Testimonio::getOrden).max().orElse(0);
            testimonio.setOrden(maxOrden + 1);
        }
        return repo.save(testimonio);
    }

    public void eliminar(Long id) {
        repo.findById(id).ifPresent(t -> {
            if (t.getFotoUrl() != null) fileStorage.eliminarArchivo(t.getFotoUrl());
            repo.delete(t);
        });
    }

    public void cambiarActivo(Long id, boolean activo) {
        repo.findById(id).ifPresent(t -> { t.setActivo(activo); repo.save(t); });
    }

    public void reordenar(Long id, int direccion) {
        List<Testimonio> lista = repo.findAllByOrderByOrdenAsc();
        int idx = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(id)) { idx = i; break; }
        }
        if (idx < 0) return;
        int target = idx + direccion;
        if (target < 0 || target >= lista.size()) return;
        Testimonio a = lista.get(idx);
        Testimonio b = lista.get(target);
        int tmp = a.getOrden();
        a.setOrden(b.getOrden());
        b.setOrden(tmp);
        repo.save(a);
        repo.save(b);
    }
}
