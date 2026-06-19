package com.ecommerce.fulltoldos.controller;
import com.ecommerce.fulltoldos.model.Producto;
import com.ecommerce.fulltoldos.model.ProductoMedia;
import com.ecommerce.fulltoldos.service.CategoriaService;
import com.ecommerce.fulltoldos.service.ConfiguracionService;
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

    @Autowired
    private ConfiguracionService configService;

    // ── Página principal (tienda) ──
    @GetMapping("/")
    public String inicio(Model model) {
        // Datos de contacto desde la DB (editables desde /admin/ajustes)
        String wspNum = configService.get("cotizacion.whatsapp", "51999999999");
        model.addAttribute("wspLink", "https://wa.me/" + wspNum);

        Map<String, String> topbar = new HashMap<>();
        topbar.put("telefono", configService.get("topbar.telefono", "+51 999 999 999"));
        topbar.put("horario",  configService.get("topbar.horario",  "Lun–Sáb 8am–7pm"));
        topbar.put("envios",   configService.get("topbar.envios",   "Envíos a todo el Perú"));
        model.addAttribute("topbar", topbar);

        Map<String, String> redes = new HashMap<>();
        redes.put("whatsapp",  configService.get("redes.whatsapp",  "https://wa.me/" + wspNum));
        redes.put("facebook",  configService.get("redes.facebook",  "#"));
        redes.put("instagram", configService.get("redes.instagram", "#"));
        redes.put("tiktok",    configService.get("redes.tiktok",    "#"));
        redes.put("youtube",   configService.get("redes.youtube",   "#"));
        model.addAttribute("redes", redes);
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
                m.put("descLarga", p.getDescripcionLarga() != null && !p.getDescripcionLarga().isBlank()
                    ? p.getDescripcionLarga() : desc);
                m.put("icon", "bi-box-seam");
                m.put("esNuevo",      p.isEsNuevo());
                m.put("esDestacado",  p.isEsDestacado());
                m.put("descuento",    p.getDescuento() != null ? p.getDescuento() : 0);
                m.put("stock",        p.getStock());
                m.put("precioOferta", p.getPrecioConDescuento());
                m.put("fechaFinOferta", p.getFechaFinOferta() != null
                    ? p.getFechaFinOferta().toString() : null);
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
                m.put("subcat", p.getSubcategoria() != null ? String.valueOf(p.getSubcategoria().getId()) : null);
                m.put("subcatName", p.getSubcategoria() != null ? p.getSubcategoria().getNombre() : null);
                m.put("unidad", p.getUnidadMedida() != null ? p.getUnidadMedida() : "UNIDAD");
                return m;
            }).collect(Collectors.toList());

        model.addAttribute("dbCats", dbCats);
        model.addAttribute("dbProducts", dbProducts);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registroForm() {
        return "registro";
    }
    @PostMapping("/registro")
    public String registroProcesar(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmar,
            Model model) {

        if (!password.equals(confirmar)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "registro";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            return "registro";
        }

        try {
            usuarioService.registrarCliente(nombre, email, password);
            return "redirect:/login?registro";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }
}