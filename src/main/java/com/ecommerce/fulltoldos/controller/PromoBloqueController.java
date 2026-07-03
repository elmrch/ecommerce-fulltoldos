package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.PromoBloque;
import com.ecommerce.fulltoldos.service.PromoBloqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/promos")
public class PromoBloqueController {

    @Autowired private PromoBloqueService service;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("bloques", service.listarTodos());
        return "admin/promos/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo(Model model) {
        model.addAttribute("bloque", new PromoBloque());
        model.addAttribute("titulo", "Nuevo Bloque");
        model.addAttribute("accion", "nuevo");
        return "admin/promos/form";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return service.buscarPorId(id).map(b -> {
            model.addAttribute("bloque", b);
            model.addAttribute("titulo", "Editar Bloque");
            model.addAttribute("accion", "editar");
            return "admin/promos/form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Bloque no encontrado");
            return "redirect:/admin/promos";
        });
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute PromoBloque bloque,
                          @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                          @RequestParam(value = "activo", required = false) String activoParam,
                          RedirectAttributes flash) {
        try {
            bloque.setActivo("true".equals(activoParam));
            service.guardar(bloque, imagen);
            flash.addFlashAttribute("ok", "Bloque guardado correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/promos";
    }

    @PostMapping("/{id}/activo")
    public String cambiarActivo(@PathVariable Long id,
                                @RequestParam boolean activo,
                                RedirectAttributes flash) {
        service.cambiarActivo(id, activo);
        flash.addFlashAttribute("ok", activo ? "Bloque activado" : "Bloque desactivado");
        return "redirect:/admin/promos";
    }

    @PostMapping("/{id}/subir")
    public String subir(@PathVariable Long id, RedirectAttributes flash) {
        service.reordenar(id, -1);
        return "redirect:/admin/promos";
    }

    @PostMapping("/{id}/bajar")
    public String bajar(@PathVariable Long id, RedirectAttributes flash) {
        service.reordenar(id, 1);
        return "redirect:/admin/promos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        service.eliminar(id);
        flash.addFlashAttribute("ok", "Bloque eliminado");
        return "redirect:/admin/promos";
    }
}