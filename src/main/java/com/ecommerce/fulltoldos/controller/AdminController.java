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
