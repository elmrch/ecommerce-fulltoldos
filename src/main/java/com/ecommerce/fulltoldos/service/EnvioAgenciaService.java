package com.ecommerce.fulltoldos.service;

import com.ecommerce.fulltoldos.model.EnvioAgencia;
import com.ecommerce.fulltoldos.repository.EnvioAgenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class EnvioAgenciaService {

    @Autowired
    private EnvioAgenciaRepository repo;

    @Autowired
    private FileStorageService fileStorage;

    public List<EnvioAgencia> listarTodos() {
        return repo.findAllByOrderByOrdenAsc();
    }

    public List<EnvioAgencia> listarActivos() {
        return repo.findByActivoTrueOrderByOrdenAsc();
    }

    public Optional<EnvioAgencia> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public EnvioAgencia guardar(EnvioAgencia agencia, MultipartFile logo) {
        if (logo != null && !logo.isEmpty()) {
            if (!fileStorage.esImagenValida(logo)) {
                throw new RuntimeException("Formato de imagen no válido");
            }
            agencia.setLogoUrl(fileStorage.guardarArchivo(logo, "agencias"));
        }
        if (agencia.getId() == null && agencia.getOrden() == 0) {
            int maxOrden = repo.findAllByOrderByOrdenAsc().stream()
                    .mapToInt(EnvioAgencia::getOrden).max().orElse(0);
            agencia.setOrden(maxOrden + 1);
        }
        return repo.save(agencia);
    }

    public void eliminar(Long id) {
        repo.findById(id).ifPresent(a -> {
            if (a.getLogoUrl() != null) fileStorage.eliminarArchivo(a.getLogoUrl());
            repo.delete(a);
        });
    }

    public void cambiarActivo(Long id, boolean activo) {
        repo.findById(id).ifPresent(a -> { a.setActivo(activo); repo.save(a); });
    }

    public void reordenar(Long id, int direccion) {
        List<EnvioAgencia> lista = repo.findAllByOrderByOrdenAsc();
        int idx = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(id)) { idx = i; break; }
        }
        if (idx < 0) return;
        int target = idx + direccion;
        if (target < 0 || target >= lista.size()) return;
        EnvioAgencia a = lista.get(idx);
        EnvioAgencia b = lista.get(target);
        int tmp = a.getOrden();
        a.setOrden(b.getOrden());
        b.setOrden(tmp);
        repo.save(a);
        repo.save(b);
    }
}
