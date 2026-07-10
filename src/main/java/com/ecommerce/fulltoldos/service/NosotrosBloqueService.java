package com.ecommerce.fulltoldos.service;

import com.ecommerce.fulltoldos.model.NosotrosBloque;
import com.ecommerce.fulltoldos.repository.NosotrosBloqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class NosotrosBloqueService {

    @Autowired
    private NosotrosBloqueRepository repo;

    @Autowired
    private FileStorageService fileStorage;

    public List<NosotrosBloque> listarTodos() {
        return repo.findAllByOrderByOrdenAsc();
    }

    public List<NosotrosBloque> listarActivos() {
        return repo.findByActivoTrueOrderByOrdenAsc();
    }

    public Optional<NosotrosBloque> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public NosotrosBloque guardar(NosotrosBloque bloque, MultipartFile imagen) {
        if (imagen != null && !imagen.isEmpty()) {
            if (!fileStorage.esImagenValida(imagen)) {
                throw new RuntimeException("Formato de imagen no válido");
            }
            bloque.setImagenUrl(fileStorage.guardarArchivo(imagen, "nosotros"));
        }
        if (bloque.getId() == null && bloque.getOrden() == 0) {
            int maxOrden = repo.findAllByOrderByOrdenAsc().stream()
                    .mapToInt(NosotrosBloque::getOrden).max().orElse(0);
            bloque.setOrden(maxOrden + 1);
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

    public void reordenar(Long id, int direccion) {
        List<NosotrosBloque> lista = repo.findAllByOrderByOrdenAsc();
        int idx = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(id)) { idx = i; break; }
        }
        if (idx < 0) return;
        int target = idx + direccion;
        if (target < 0 || target >= lista.size()) return;
        NosotrosBloque a = lista.get(idx);
        NosotrosBloque b = lista.get(target);
        int tmp = a.getOrden();
        a.setOrden(b.getOrden());
        b.setOrden(tmp);
        repo.save(a);
        repo.save(b);
    }

    /** Carga el contenido original de la página "Sobre nosotros" si todavía no hay bloques en la base de datos. */
    public void seedDefaults() {
        if (repo.count() > 0) return;
        crear(1, "/carpas2.png", "Fabricante y distribuidor de Toldos & Carpas en Perú",
                "Fabricantes y distribuidores de toldos y carpas en Lima y todo el Perú.",
                "Bienvenido al sitio web oficial de Full Toldos, tu proveedor de confianza de coberturas y estructuras textiles de alta calidad en el Perú. Nos dedicamos a crear toldos, carpas y estructuras innovadoras que mejoran cada espacio con durabilidad, funcionalidad y facilidad de instalación.");
        crear(2, "/fulltolfdos12.png", "Nuestra filosofía", null,
                "En Full Toldos, creemos que los mejores espacios comienzan con coberturas de calidad. Nuestra marca se basa en la experiencia, la innovación y la artesanía peruana. Fabricamos toldos y carpas con lona PVC de alta resistencia y acero galvanizado que ofrecen durabilidad y protección en cualquier ambiente. Ya sea para un local comercial, un evento o una estructura industrial, Full Toldos tiene la solución que se adapta a tus necesidades exactas.");
        crear(3, "/carpas2.png", "Compromiso con la calidad", null,
                "La calidad es la base de todo lo que hacemos. Cada producto Full Toldos se somete a rigurosas pruebas y una meticulosa fabricación para garantizar los más altos estándares. Nuestro objetivo es ofrecer coberturas que maximicen la durabilidad, facilidad de uso y resistencia. Utilizamos materiales certificados — lonas PVC, acero galvanizado y textiles técnicos — para garantizar que nuestros productos cumplan con las exigencias del mercado peruano.");
        crear(4, "/fulltolfdos12.png", "Diseñado para tu negocio y hogar", null,
                "Una cobertura de calidad es más que protección: es una inversión. Los toldos y carpas de Full Toldos están diseñados para quienes valoran la funcionalidad y el diseño, ya sea para proteger un negocio, amenizar un evento o ampliar un espacio exterior. Ofrecemos el equilibrio perfecto entre resistencia y estética, ideal para negocios, familias y eventos en Lima y provincias.");
        crear(5, "/carpas2.png", "Fabricación a medida", null,
                "Full Toldos es más que una empresa: es un compromiso con la cobertura perfecta. Nuestros productos están diseñados con características bien pensadas — ventilación optimizada, estructuras resistentes y diseños personalizados — para garantizar la máxima protección en cualquier entorno. Fabricamos sin medidas estándar que no se adaptan a tu espacio; cada pedido es único y se confecciona según tus especificaciones exactas.");
        crear(6, "/fulltolfdos12.png", "Cubrimos Lima y Provincias", null,
                "Elegir Full Toldos significa elegir soluciones funcionales y bien diseñadas para cualquier espacio. Como fabricantes en Lima, atendemos pedidos en toda la región. Ya sea un toldo retráctil, una carpa armable o una estructura publicitaria, Full Toldos está aquí para apoyar tu proyecto. Nuestro equipo siempre está listo para ayudarte. Contáctanos en cualquier momento; estamos aquí para que tu experiencia sea lo más satisfactoria posible.");
    }

    private void crear(int orden, String imagenUrl, String titulo, String subtitulo, String cuerpo) {
        NosotrosBloque b = new NosotrosBloque();
        b.setOrden(orden);
        b.setImagenUrl(imagenUrl);
        b.setTitulo(titulo);
        b.setSubtitulo(subtitulo);
        b.setCuerpo(cuerpo);
        repo.save(b);
    }
}
