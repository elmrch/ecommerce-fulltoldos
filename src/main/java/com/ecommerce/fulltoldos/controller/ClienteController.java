package com.ecommerce.fulltoldos.controller;


import com.ecommerce.fulltoldos.repository.CotizacionRepository;
import com.ecommerce.fulltoldos.repository.PedidoRepository;
import com.ecommerce.fulltoldos.repository.UsuarioRepository;
import com.ecommerce.fulltoldos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CotizacionRepository cotizacionRepository;

    @GetMapping("/inicio")
    public String inicio(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("emailCliente", user.getUsername());
        return "cliente/inicio"; // templates/cliente/inicio.html
    }

    @GetMapping("/carrito")
    public String carrito(Model model) {
        return "cliente/carrito";
    }

    // ── Mis pedidos ──
    @GetMapping("/pedidos")
    public String pedidos(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("emailCliente", user.getUsername());
        usuarioRepository.findByEmail(user.getUsername()).ifPresent(u ->
                model.addAttribute("pedidos", pedidoRepository.findByUsuarioOrderByFechaCreacionDesc(u)));
        return "cliente/pedidos";
    }

    // ── Mis cotizaciones ──
    @GetMapping("/cotizaciones")
    public String cotizaciones(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("emailCliente", user.getUsername());
        model.addAttribute("cotizaciones",
                cotizacionRepository.findByCorreoIgnoreCaseOrderByFechaSolicitudDesc(user.getUsername()));
        return "cliente/cotizaciones"; // templates/cliente/cotizaciones.html
    }

    // ── Mi perfil ──
    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("emailCliente", user.getUsername());
        usuarioRepository.findByEmail(user.getUsername()).ifPresent(u -> model.addAttribute("usuario", u));
        return "cliente/perfil"; // templates/cliente/perfil.html
    }

    @PostMapping("/perfil")
    public String actualizarPerfil(@AuthenticationPrincipal UserDetails user,
                                    @RequestParam String nombre,
                                    RedirectAttributes ra) {
        usuarioService.actualizarNombre(user.getUsername(), nombre);
        ra.addFlashAttribute("ok", "Perfil actualizado.");
        return "redirect:/cliente/perfil";
    }

    @PostMapping("/perfil/password")
    public String cambiarPassword(@AuthenticationPrincipal UserDetails user,
                                   @RequestParam String actual,
                                   @RequestParam String nueva,
                                   RedirectAttributes ra) {
        boolean ok = usuarioService.cambiarPassword(user.getUsername(), actual, nueva);
        if (ok) ra.addFlashAttribute("ok", "Contraseña actualizada.");
        else ra.addFlashAttribute("error", "La contraseña actual no es correcta.");
        return "redirect:/cliente/perfil";
    }
}
