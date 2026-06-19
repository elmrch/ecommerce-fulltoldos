package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.Subcategoria;
import com.ecommerce.fulltoldos.service.CategoriaService;
import com.ecommerce.fulltoldos.service.SubcategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/subcategorias")
public class SubcategoriaController {

    @Autowired
    private SubcategoriaService subcategoriaService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("subcategorias", subcategoriaService.listarTodas());
        model.addAttribute("titulo", "Subcategorías");
        return "admin/subcategorias/lista";
    }

    @GetMapping("/nueva")
    public String formNueva(Model model) {
        model.addAttribute("subcategoria", new Subcategoria());
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("titulo", "Nueva Subcategoría");
        model.addAttribute("accion", "nueva");
        return "admin/subcategorias/form";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return subcategoriaService.buscarPorId(id).map(sub -> {
            model.addAttribute("subcategoria", sub);
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("titulo", "Editar Subcategoría");
            model.addAttribute("accion", "editar");
            return "admin/subcategorias/form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Subcategoría no encontrada");
            return "redirect:/admin/subcategorias";
        });
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Subcategoria subcategoria,
                          @RequestParam("categoriaId") Long categoriaId,
                          RedirectAttributes flash) {
        try {
            categoriaService.buscarPorId(categoriaId).ifPresent(subcategoria::setCategoria);
            subcategoriaService.guardar(subcategoria);
            flash.addFlashAttribute("exito",
                    subcategoria.getId() == null ? "Subcategoría creada" : "Subcategoría actualizada");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/subcategorias";
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam boolean activo,
                                RedirectAttributes flash) {
        subcategoriaService.cambiarEstado(id, activo);
        flash.addFlashAttribute("exito", activo ? "Subcategoría activada" : "Subcategoría desactivada");
        return "redirect:/admin/subcategorias";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            subcategoriaService.eliminar(id);
            flash.addFlashAttribute("exito", "Subcategoría eliminada");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se puede eliminar: tiene productos asociados");
        }
        return "redirect:/admin/subcategorias";
    }
}