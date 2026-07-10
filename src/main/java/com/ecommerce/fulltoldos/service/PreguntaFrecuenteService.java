package com.ecommerce.fulltoldos.service;

import com.ecommerce.fulltoldos.model.PreguntaFrecuente;
import com.ecommerce.fulltoldos.repository.PreguntaFrecuenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PreguntaFrecuenteService {

    @Autowired
    private PreguntaFrecuenteRepository repo;

    public List<PreguntaFrecuente> listarTodos() {
        return repo.findAllByOrderByOrdenAsc();
    }

    public List<PreguntaFrecuente> listarActivos() {
        return repo.findByActivoTrueOrderByOrdenAsc();
    }

    public Optional<PreguntaFrecuente> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public PreguntaFrecuente guardar(PreguntaFrecuente faq) {
        if (faq.getId() == null && faq.getOrden() == 0) {
            int maxOrden = repo.findAllByOrderByOrdenAsc().stream()
                    .mapToInt(PreguntaFrecuente::getOrden).max().orElse(0);
            faq.setOrden(maxOrden + 1);
        }
        return repo.save(faq);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public void cambiarActivo(Long id, boolean activo) {
        repo.findById(id).ifPresent(f -> { f.setActivo(activo); repo.save(f); });
    }

    public void reordenar(Long id, int direccion) {
        List<PreguntaFrecuente> lista = repo.findAllByOrderByOrdenAsc();
        int idx = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(id)) { idx = i; break; }
        }
        if (idx < 0) return;
        int target = idx + direccion;
        if (target < 0 || target >= lista.size()) return;
        PreguntaFrecuente a = lista.get(idx);
        PreguntaFrecuente b = lista.get(target);
        int tmp = a.getOrden();
        a.setOrden(b.getOrden());
        b.setOrden(tmp);
        repo.save(a);
        repo.save(b);
    }

    /** Carga las preguntas frecuentes originales si todavía no hay ninguna en la base de datos. */
    public void seedDefaults() {
        if (repo.count() > 0) return;
        crear(1, "¿Cuántos años lleva Full Toldos en el mercado?",
                "Full Toldos tiene más de 25 años fabricando toldos, carpas y estructuras textiles en el Perú. Nuestra experiencia nos avala como referentes del sector en Lima y provincias.");
        crear(2, "¿Cómo obtener una cotización personalizada?",
                "Puedes solicitar tu cotización por WhatsApp o usando el formulario de contacto en nuestra web. Respondemos en menos de 24 horas con precios claros, sin intermediarios y sin compromiso.");
        crear(3, "¿Fabrican a medida?",
                "Sí, todos nuestros productos se fabrican a medida. No trabajamos con medidas estándar que no se adapten a tu espacio. Desde estructuras pequeñas hasta grandes carpas industriales, todo se confecciona según tus especificaciones.");
        crear(4, "¿Hacen envíos a provincias?",
                "Sí, atendemos pedidos en todo el Perú. Coordinamos el envío e instalación (si se requiere) tanto en Lima como en provincias. Contáctanos para más detalles sobre tiempos y costos de envío a tu región.");
    }

    private void crear(int orden, String pregunta, String respuesta) {
        PreguntaFrecuente f = new PreguntaFrecuente();
        f.setOrden(orden);
        f.setPregunta(pregunta);
        f.setRespuesta(respuesta);
        repo.save(f);
    }
}
