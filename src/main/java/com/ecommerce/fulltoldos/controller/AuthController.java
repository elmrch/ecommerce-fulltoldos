package com.ecommerce.fulltoldos.controller;
import com.ecommerce.fulltoldos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // ── Página principal (tienda) ──
    @GetMapping("/")
    public String inicio() {
        return "index"; // templates/index.html
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