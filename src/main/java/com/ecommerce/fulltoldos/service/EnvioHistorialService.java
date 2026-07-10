package com.ecommerce.fulltoldos.service;

import com.ecommerce.fulltoldos.model.EnvioHistorial;
import com.ecommerce.fulltoldos.repository.EnvioHistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class EnvioHistorialService {

    @Autowired
    private EnvioHistorialRepository repo;

    @Autowired
    private FileStorageService fileStorage;

    public List<EnvioHistorial> listarTodos() {
        return repo.findAllByOrderByOrdenAsc();
    }

    public List<EnvioHistorial> listarActivos() {
        return repo.findByActivoTrueOrderByOrdenAsc();
    }

    public Optional<EnvioHistorial> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public EnvioHistorial guardar(EnvioHistorial envio, MultipartFile imagen) {
        if (imagen != null && !imagen.isEmpty()) {
            if (!fileStorage.esImagenValida(imagen)) {
                throw new RuntimeException("Formato de imagen no válido");
            }
            envio.setImagenUrl(fileStorage.guardarArchivo(imagen, "envios"));
        }
        if (envio.getId() == null && envio.getOrden() == 0) {
            int maxOrden = repo.findAllByOrderByOrdenAsc().stream()
                    .mapToInt(EnvioHistorial::getOrden).max().orElse(0);
            envio.setOrden(maxOrden + 1);
        }
        return repo.save(envio);
    }

    public void eliminar(Long id) {
        repo.findById(id).ifPresent(e -> {
            if (e.getImagenUrl() != null) fileStorage.eliminarArchivo(e.getImagenUrl());
            repo.delete(e);
        });
    }

    public void cambiarActivo(Long id, boolean activo) {
        repo.findById(id).ifPresent(e -> { e.setActivo(activo); repo.save(e); });
    }

    public void reordenar(Long id, int direccion) {
        List<EnvioHistorial> lista = repo.findAllByOrderByOrdenAsc();
        int idx = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(id)) { idx = i; break; }
        }
        if (idx < 0) return;
        int target = idx + direccion;
        if (target < 0 || target >= lista.size()) return;
        EnvioHistorial a = lista.get(idx);
        EnvioHistorial b = lista.get(target);
        int tmp = a.getOrden();
        a.setOrden(b.getOrden());
        b.setOrden(tmp);
        repo.save(a);
        repo.save(b);
    }
}
