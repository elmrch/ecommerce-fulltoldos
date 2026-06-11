package com.ecommerce.fulltoldos.controller;
import com.ecommerce.fulltoldos.model.Producto;
import com.ecommerce.fulltoldos.model.ProductoMedia;
import com.ecommerce.fulltoldos.service.CategoriaService;
import com.ecommerce.fulltoldos.service.ProductoService;
import com.ecommerce.fulltoldos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoService productoService;

    // ── Página principal (tienda) ──
    @GetMapping("/")
    public String inicio(Model model) {
        List<Producto> prods = productoService.listarActivos();

        // Contar productos por categoría
        Map<Long, Long> countByCat = prods.stream()
            .collect(Collectors.groupingBy(p -> p.getCategoria().getId(), Collectors.counting()));

        // Categorías activas → formato JS
        List<Map<String, Object>> dbCats = categoriaService.listarActivas().stream()
            .map(cat -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", String.valueOf(cat.getId()));
                m.put("name", cat.getNombre());
                m.put("img", cat.getImagenUrl() != null ? cat.getImagenUrl() : "");
                m.put("color", "linear-gradient(135deg,#0e3460,#1565c0)");
                m.put("icon", "bi-tags");
                m.put("count", countByCat.getOrDefault(cat.getId(), 0L).intValue());
                return m;
            }).collect(Collectors.toList());

        // Productos activos → formato JS
        List<Map<String, Object>> dbProducts = prods.stream()
            .map(p -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", p.getId());
                m.put("name", p.getNombre());
                m.put("cat", String.valueOf(p.getCategoria().getId()));
                m.put("catName", p.getCategoria().getNombre());
                String desc = p.getDescripcion() != null ? p.getDescripcion() : "";
                m.put("desc", desc);
                m.put("longDesc", desc);
                m.put("icon", "bi-box-seam");
                m.put("badge", null);
                List<String> features = Collections.emptyList();
                if (p.getCaracteristicas() != null && !p.getCaracteristicas().trim().isEmpty()) {
                    features = Arrays.stream(p.getCaracteristicas().split("[,\n]"))
                        .map(String::trim).filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                }
                m.put("features", features);
                List<String> medidas = Collections.emptyList();
                if (p.getMedidas() != null && !p.getMedidas().trim().isEmpty()) {
                    medidas = Arrays.stream(p.getMedidas().split(","))
                        .map(String::trim).filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                }
                m.put("medidas", medidas);
                m.put("colores", Collections.emptyList());
                // Principal primero, luego el resto en orden
                List<String> imgs = new ArrayList<>();
                if (p.getMedias() != null) {
                    p.getMedias().stream()
                        .filter(media -> "imagen".equals(media.getTipo()) && media.isPrincipal())
                        .map(ProductoMedia::getUrl)
                        .forEach(imgs::add);
                    p.getMedias().stream()
                        .filter(media -> "imagen".equals(media.getTipo()) && !media.isPrincipal())
                        .map(ProductoMedia::getUrl)
                        .forEach(imgs::add);
                }
                m.put("imgs", imgs);
                List<String> videos = new ArrayList<>();
                if (p.getMedias() != null) {
                    p.getMedias().stream()
                        .filter(media -> "video".equals(media.getTipo()))
                        .map(ProductoMedia::getUrl)
                        .forEach(videos::add);
                }
                m.put("videos", videos);
                m.put("precio", p.getPrecio());
                m.put("rating", null);
                m.put("reviews", 0);
                return m;
            }).collect(Collectors.toList());

        model.addAttribute("dbCats", dbCats);
        model.addAttribute("dbProducts", dbProducts);
        return "index";
    }

    // ── Mostrar formulario login ──
    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }

    // ── Mostrar formulario registro ──
    @GetMapping("/registro")
    public String registroForm() {
        return "registro"; // templates/registro.html
    }

    // ── Procesar formulario registro ──
    @PostMapping("/registro")
    public String registroProcesar(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmar,
            Model model) {

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmar)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "registro";
        }

        // Validar longitud mínima
        if (password.length() < 6) {
            model.addAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            return "registro";
        }

        try {
            usuarioService.registrarCliente(nombre, email, password);
            // Redirige al login con mensaje de éxito
            return "redirect:/login?registro";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }

    // ── Acceso denegado ──
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado"; // templates/acceso-denegado.html
    }
}