package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.PreguntaFrecuente;
import com.ecommerce.fulltoldos.service.PreguntaFrecuenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/faq")
public class FaqAdminController {

    @Autowired private PreguntaFrecuenteService faqService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("faqs", faqService.listarTodos());
        return "admin/faq/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("faq", new PreguntaFrecuente());
        model.addAttribute("titulo", "Nueva Pregunta");
        return "admin/faq/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return faqService.buscarPorId(id).map(f -> {
            model.addAttribute("faq", f);
            model.addAttribute("titulo", "Editar Pregunta");
            return "admin/faq/form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Pregunta no encontrada");
            return "redirect:/admin/faq";
        });
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute PreguntaFrecuente faq,
                           @RequestParam(value = "activo", required = false) String activoParam,
                           RedirectAttributes flash) {
        try {
            faq.setActivo("true".equals(activoParam));
            faqService.guardar(faq);
            flash.addFlashAttribute("ok", faq.getId() == null ? "Pregunta creada" : "Pregunta actualizada");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/faq";
    }

    @PostMapping("/{id}/activo")
    public String activo(@PathVariable Long id, @RequestParam boolean activo, RedirectAttributes flash) {
        faqService.cambiarActivo(id, activo);
        flash.addFlashAttribute("ok", activo ? "Pregunta activada" : "Pregunta desactivada");
        return "redirect:/admin/faq";
    }

    @PostMapping("/{id}/subir")
    public String subir(@PathVariable Long id) {
        faqService.reordenar(id, -1);
        return "redirect:/admin/faq";
    }

    @PostMapping("/{id}/bajar")
    public String bajar(@PathVariable Long id) {
        faqService.reordenar(id, 1);
        return "redirect:/admin/faq";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        faqService.eliminar(id);
        flash.addFlashAttribute("ok", "Pregunta eliminada");
        return "redirect:/admin/faq";
    }
}
