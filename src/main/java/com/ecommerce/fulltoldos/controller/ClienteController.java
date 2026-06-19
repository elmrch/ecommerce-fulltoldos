package com.ecommerce.fulltoldos.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

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
    public String pedidos(Model model) {
        return "cliente/pedidos";
    }



    @GetMapping("/cotizaciones")
    public String cotizaciones(Model model) {
        return "cliente/cotizaciones"; // templates/cliente/cotizaciones.html
    }

    // ── Mi perfil ──
    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("emailCliente", user.getUsername());
        return "cliente/perfil"; // templates/cliente/perfil.html
    }
}