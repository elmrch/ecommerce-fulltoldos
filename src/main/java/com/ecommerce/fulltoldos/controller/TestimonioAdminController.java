package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.Testimonio;
import com.ecommerce.fulltoldos.service.TestimonioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/testimonios")
public class TestimonioAdminController {

    @Autowired
    private TestimonioService testimonioService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("testimonios", testimonioService.listarTodos());
        return "admin/testimonios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("testimonio", new Testimonio());
        model.addAttribute("titulo", "Nuevo Testimonio");
        return "admin/testimonios/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return testimonioService.buscarPorId(id).map(t -> {
            model.addAttribute("testimonio", t);
            model.addAttribute("titulo", "Editar Testimonio");
            return "admin/testimonios/form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Testimonio no encontrado");
            return "redirect:/admin/testimonios";
        });
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Testimonio testimonio,
                           @RequestParam(value = "foto", required = false) MultipartFile foto,
                           @RequestParam(value = "activo", required = false) String activoParam,
                           RedirectAttributes flash) {
        try {
            testimonio.setActivo("true".equals(activoParam));
            testimonioService.guardar(testimonio, foto);
            flash.addFlashAttribute("ok", testimonio.getId() == null ? "Testimonio creado" : "Testimonio actualizado");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/testimonios";
    }

    @PostMapping("/{id}/activo")
    public String activo(@PathVariable Long id, @RequestParam boolean activo, RedirectAttributes flash) {
        testimonioService.cambiarActivo(id, activo);
        flash.addFlashAttribute("ok", activo ? "Testimonio activado" : "Testimonio desactivado");
        return "redirect:/admin/testimonios";
    }

    @PostMapping("/{id}/subir")
    public String subir(@PathVariable Long id) {
        testimonioService.reordenar(id, -1);
        return "redirect:/admin/testimonios";
    }

    @PostMapping("/{id}/bajar")
    public String bajar(@PathVariable Long id) {
        testimonioService.reordenar(id, 1);
        return "redirect:/admin/testimonios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        testimonioService.eliminar(id);
        flash.addFlashAttribute("ok", "Testimonio eliminado");
        return "redirect:/admin/testimonios";
    }
}
