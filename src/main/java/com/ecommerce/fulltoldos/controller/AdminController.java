package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.Configuracion;
import com.ecommerce.fulltoldos.model.Pedido;
import com.ecommerce.fulltoldos.repository.CategoriaRepository;
import com.ecommerce.fulltoldos.repository.CotizacionRepository;
import com.ecommerce.fulltoldos.repository.PedidoRepository;
import com.ecommerce.fulltoldos.repository.ProductoRepository;
import com.ecommerce.fulltoldos.service.ConfiguracionService;
import com.ecommerce.fulltoldos.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ConfiguracionService configService;

    @Autowired
    private CotizacionRepository cotizacionRepository;

    @Autowired
    private FileStorageService fileStorage;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("emailAdmin", user.getUsername());
        model.addAttribute("totalCotizaciones", cotizacionRepository.count());
        model.addAttribute("totalPedidos", pedidoRepository.count());
        return "admin/dashboard";
    }

    /* ── Ajustes ── */
    @GetMapping("/ajustes")
    public String ajustes(Model model) {
        model.addAttribute("configs", configService.todos());
        model.addAttribute("bannerImagen",    configService.get("banner.central.imagen",    "/img/fulltoldos.png"));
        model.addAttribute("bannerLinkTipo",  configService.get("banner.central.link.tipo", "whatsapp"));
        model.addAttribute("bannerLinkValor", configService.get("banner.central.link.valor",""));
        model.addAttribute("newsletterImagen", configService.get("newsletter.imagen", "/img/imgfooter.png"));
        model.addAttribute("logoImagen",   configService.get("sitio.logo",          "/logofulltoldos.png"));
        model.addAttribute("logoPosicion", configService.get("sitio.logo.posicion", "left"));

        model.addAttribute("buildBannerImagen",      configService.get("build.banner.imagen",      "/img/construimos.png"));
        model.addAttribute("buildBannerTitulo",       configService.get("build.banner.titulo",       "FABRICADO<br>MEJOR.<br>FABRICADO DE<br>FORMA DIFERENTE."));
        model.addAttribute("buildBannerDescripcion",  configService.get("build.banner.descripcion",  "Full Toldos, una marca auténtica y original, líder en estructuras de cobertura y toldos a medida en el Perú. Llevamos más de 25 años construyendo sobre nuestro legado de innovación y experiencia en fabricación con productos que marcan la pauta en calidad, durabilidad y facilidad de uso."));
        model.addAttribute("buildBannerBtnTexto",     configService.get("build.banner.btn.texto",     "Más información"));
        model.addAttribute("buildBannerBtnTipo",      configService.get("build.banner.btn.tipo",      "cotizador"));
        model.addAttribute("buildBannerBtnValor",     configService.get("build.banner.btn.valor",     ""));
        model.addAttribute("buildBannerChatTitulo",   configService.get("build.banner.chat.titulo",   "Chatea con un experto."));
        model.addAttribute("buildBannerChatSub",      configService.get("build.banner.chat.sub",      "Asesoría personalizada por WhatsApp sin costo."));

        model.addAttribute("categorias", categoriaRepository.findByActivoTrueOrderByOrdenAsc());
        model.addAttribute("productos",  productoRepository.findByActivoTrueOrderByFechaCreacionDesc());
        return "admin/ajustes";
    }

    @PostMapping("/ajustes")
    public String guardarAjustes(@RequestParam Map<String, String> params,
                                  RedirectAttributes ra) {
        params.forEach((clave, valor) -> {
            if (!clave.startsWith("_")) {           // ignorar _csrf etc.
                configService.guardar(clave, valor);
            }
        });
        ra.addFlashAttribute("ok", "Ajustes guardados correctamente.");
        return "redirect:/admin/ajustes";
    }

    @PostMapping("/ajustes/banner")
    public String guardarBanner(@RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                @RequestParam(value = "bannerLinkTipo",  defaultValue = "") String linkTipo,
                                @RequestParam(value = "bannerLinkValor", defaultValue = "") String linkValor,
                                RedirectAttributes ra) {
        try {
            if (imagen != null && !imagen.isEmpty()) {
                if (!fileStorage.esImagenValida(imagen))
                    throw new RuntimeException("Formato de imagen no válido");
                String url = fileStorage.guardarArchivo(imagen, "banner");
                configService.guardar("banner.central.imagen", url);
            }
            configService.guardar("banner.central.link.tipo",  linkTipo);
            configService.guardar("banner.central.link.valor", linkValor);
            ra.addFlashAttribute("ok", "Banner central actualizado.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al guardar el banner: " + e.getMessage());
        }
        return "redirect:/admin/ajustes";
    }

    @PostMapping("/ajustes/build-banner")
    public String guardarBuildBanner(@RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                      @RequestParam(value = "buildBannerTitulo",      defaultValue = "") String titulo,
                                      @RequestParam(value = "buildBannerDescripcion", defaultValue = "") String descripcion,
                                      @RequestParam(value = "buildBannerBtnTexto",    defaultValue = "") String btnTexto,
                                      @RequestParam(value = "buildBannerBtnTipo",     defaultValue = "cotizador") String btnTipo,
                                      @RequestParam(value = "buildBannerBtnValor",    defaultValue = "") String btnValor,
                                      @RequestParam(value = "buildBannerChatTitulo",  defaultValue = "") String chatTitulo,
                                      @RequestParam(value = "buildBannerChatSub",     defaultValue = "") String chatSub,
                                      RedirectAttributes ra) {
        try {
            if (imagen != null && !imagen.isEmpty()) {
                if (!fileStorage.esImagenValida(imagen))
                    throw new RuntimeException("Formato de imagen no válido");
                String url = fileStorage.guardarArchivo(imagen, "banner");
                configService.guardar("build.banner.imagen", url);
            }
            configService.guardar("build.banner.titulo", titulo);
            configService.guardar("build.banner.descripcion", descripcion);
            configService.guardar("build.banner.btn.texto", btnTexto);
            configService.guardar("build.banner.btn.tipo", btnTipo);
            configService.guardar("build.banner.btn.valor", btnValor);
            configService.guardar("build.banner.chat.titulo", chatTitulo);
            configService.guardar("build.banner.chat.sub", chatSub);
            ra.addFlashAttribute("ok", "Banner actualizado.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al guardar el banner: " + e.getMessage());
        }
        return "redirect:/admin/ajustes";
    }

    @PostMapping("/ajustes/logo")
    public String guardarLogo(@RequestParam(value = "imagen", required = false) MultipartFile imagen,
                               @RequestParam(value = "logoPosicion", defaultValue = "left") String posicion,
                               RedirectAttributes ra) {
        try {
            if (imagen != null && !imagen.isEmpty()) {
                if (!fileStorage.esImagenValida(imagen))
                    throw new RuntimeException("Formato de imagen no válido");
                String url = fileStorage.guardarArchivo(imagen, "logo");
                configService.guardar("sitio.logo", url);
            }
            configService.guardar("sitio.logo.posicion", posicion);
            ra.addFlashAttribute("ok", "Logo actualizado.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al guardar el logo: " + e.getMessage());
        }
        return "redirect:/admin/ajustes";
    }

    @PostMapping("/ajustes/newsletter-imagen")
    public String guardarNewsletterImagen(@RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                           RedirectAttributes ra) {
        try {
            if (imagen != null && !imagen.isEmpty()) {
                if (!fileStorage.esImagenValida(imagen))
                    throw new RuntimeException("Formato de imagen no válido");
                String url = fileStorage.guardarArchivo(imagen, "newsletter");
                configService.guardar("newsletter.imagen", url);
                ra.addFlashAttribute("ok", "Imagen del newsletter actualizada.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al guardar la imagen: " + e.getMessage());
        }
        return "redirect:/admin/ajustes";
    }

    /* ── Cotizaciones ── */
    @GetMapping("/cotizaciones")
    public String cotizaciones(Model model) {
        model.addAttribute("cotizaciones", cotizacionRepository.findAllByOrderByFechaSolicitudDesc());
        return "admin/cotizaciones";
    }

    @PostMapping("/cotizaciones/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                 @RequestParam String estado,
                                 RedirectAttributes ra) {
        cotizacionRepository.findById(id).ifPresent(c -> {
            c.setEstado(com.ecommerce.fulltoldos.model.Cotizacion.Estado.valueOf(estado));
            cotizacionRepository.save(c);
        });
        ra.addFlashAttribute("ok", "Estado actualizado.");
        return "redirect:/admin/cotizaciones";
    }

    /* ── Pedidos ── */
    @GetMapping("/pedidos")
    public String pedidos(Model model) {
        model.addAttribute("pedidos", pedidoRepository.findAllByOrderByFechaCreacionDesc());
        return "admin/pedidos";
    }

    @PostMapping("/pedidos/{id}/estado")
    public String cambiarEstadoPedido(@PathVariable Long id,
                                       @RequestParam String estado,
                                       RedirectAttributes ra) {
        pedidoRepository.findById(id).ifPresent(p -> {
            p.setEstado(Pedido.Estado.valueOf(estado));
            pedidoRepository.save(p);
        });
        ra.addFlashAttribute("ok", "Estado actualizado.");
        return "redirect:/admin/pedidos";
    }
}
