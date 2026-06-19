package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.Rol;
import com.ecommerce.fulltoldos.model.Usuario;
import com.ecommerce.fulltoldos.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ── Listar todos ──
    @GetMapping
    public String listar(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        long totalClientes = usuarios.stream()
            .filter(u -> u.getRol() != null && u.getRol().name().equals("ROLE_CLIENTE")).count();
        long totalAdmins = usuarios.stream()
            .filter(u -> u.getRol() != null && u.getRol().name().equals("ROLE_ADMIN")).count();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalAdmins", totalAdmins);
        model.addAttribute("titulo", "Usuarios");
        return "admin/usuarios/lista";
    }

    // ── Ver detalle ──
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model,
                          RedirectAttributes flash) {
        return usuarioRepository.findById(id).map(u -> {
            model.addAttribute("usuario", u);
            return "admin/usuarios/detalle";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/admin/usuarios";
        });
    }

    // ── Activar / Desactivar ──
    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam boolean activo,
                                RedirectAttributes flash) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setActivo(activo);
            usuarioRepository.save(u);
        });
        flash.addFlashAttribute("exito",
                activo ? "Usuario activado" : "Usuario desactivado");
        return "redirect:/admin/usuarios";
    }

    // ── Cambiar rol ──
    @PostMapping("/rol/{id}")
    public String cambiarRol(@PathVariable Long id,
                             @RequestParam String rol,
                             RedirectAttributes flash) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setRol(Rol.valueOf(rol));
            usuarioRepository.save(u);
        });
        flash.addFlashAttribute("exito", "Rol actualizado");
        return "redirect:/admin/usuarios";
    }

    // ── Eliminar ──
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            usuarioRepository.deleteById(id);
            flash.addFlashAttribute("exito", "Usuario eliminado");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se puede eliminar este usuario");
        }
        return "redirect:/admin/usuarios";
    }

    // ── Resetear password ──
    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable Long id,
                                @RequestParam String nuevaPassword,
                                RedirectAttributes flash) {
        if (nuevaPassword.length() < 6) {
            flash.addFlashAttribute("error", "La password debe tener al menos 6 caracteres");
            return "redirect:/admin/usuarios/detalle/" + id;
        }
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setPassword(passwordEncoder.encode(nuevaPassword));
            usuarioRepository.save(u);
        });
        flash.addFlashAttribute("exito", "Password actualizado");
        return "redirect:/admin/usuarios/detalle/" + id;
    }
}