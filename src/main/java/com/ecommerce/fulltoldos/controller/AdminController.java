package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.Configuracion;
import com.ecommerce.fulltoldos.repository.CotizacionRepository;
import com.ecommerce.fulltoldos.service.ConfiguracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("emailAdmin", user.getUsername());
        model.addAttribute("totalCotizaciones", cotizacionRepository.count());
        return "admin/dashboard";
    }

    /* ── Ajustes ── */
    @GetMapping("/ajustes")
    public String ajustes(Model model) {
        model.addAttribute("configs", configService.todos());
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
}
