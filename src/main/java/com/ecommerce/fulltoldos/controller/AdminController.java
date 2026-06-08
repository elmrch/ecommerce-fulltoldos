package com.ecommerce.fulltoldos.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // ── Dashboard principal ──
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("emailAdmin", user.getUsername());
        return "admin/dashboard"; // templates/admin/dashboard.html
    }

    // ── Gestión de productos ──
    @GetMapping("/productos")
    public String productos(Model model) {
        return "admin/productos"; // templates/admin/productos.html
    }

    // ── Gestión de pedidos ──
    @GetMapping("/pedidos")
    public String pedidos(Model model) {
        return "admin/pedidos"; // templates/admin/pedidos.html
    }

    // ── Gestión de usuarios ──
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        return "admin/usuarios"; // templates/admin/usuarios.html
    }

    // ── Cotizaciones ──
    @GetMapping("/cotizaciones")
    public String cotizaciones(Model model) {
        return "admin/cotizaciones"; // templates/admin/cotizaciones.html
    }
}
