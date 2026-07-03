package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.Slide;
import com.ecommerce.fulltoldos.repository.CategoriaRepository;
import com.ecommerce.fulltoldos.repository.ProductoRepository;
import com.ecommerce.fulltoldos.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/slides")
public class SlideController {

    @Autowired
    private SlideService slideService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("slides", slideService.listarTodos());
        return "admin/slides/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo(Model model) {
        model.addAttribute("slide", new Slide());
        model.addAttribute("titulo", "Nuevo Slide");
        model.addAttribute("accion", "nuevo");
        agregarOpcionesEnlace(model);
        return "admin/slides/form";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return slideService.buscarPorId(id).map(slide -> {
            model.addAttribute("slide", slide);
            model.addAttribute("titulo", "Editar Slide");
            model.addAttribute("accion", "editar");
            agregarOpcionesEnlace(model);
            return "admin/slides/form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Slide no encontrado");
            return "redirect:/admin/slides";
        });
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Slide slide,
                          @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                          @RequestParam(value = "activo", required = false) String activoParam,
                          RedirectAttributes flash) {
        try {
            slide.setActivo("true".equals(activoParam));
            slideService.guardar(slide, imagen);
            flash.addFlashAttribute("ok", slide.getId() == null ? "Slide creado" : "Slide actualizado");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/slides";
    }

    @PostMapping("/{id}/activo")
    public String cambiarActivo(@PathVariable Long id,
                                @RequestParam boolean activo,
                                RedirectAttributes flash) {
        slideService.cambiarActivo(id, activo);
        flash.addFlashAttribute("ok", activo ? "Slide activado" : "Slide desactivado");
        return "redirect:/admin/slides";
    }

    @PostMapping("/{id}/subir")
    public String subir(@PathVariable Long id, RedirectAttributes flash) {
        slideService.reordenar(id, -1);
        return "redirect:/admin/slides";
    }

    @PostMapping("/{id}/bajar")
    public String bajar(@PathVariable Long id, RedirectAttributes flash) {
        slideService.reordenar(id, 1);
        return "redirect:/admin/slides";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        slideService.eliminar(id);
        flash.addFlashAttribute("ok", "Slide eliminado");
        return "redirect:/admin/slides";
    }

    private void agregarOpcionesEnlace(Model model) {
        model.addAttribute("categorias", categoriaRepository.findByActivoTrueOrderByOrdenAsc());
        model.addAttribute("productos", productoRepository.findByActivoTrueOrderByFechaCreacionDesc());
    }
}