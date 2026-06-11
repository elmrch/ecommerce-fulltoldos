package com.ecommerce.fulltoldos.controller;


import com.ecommerce.fulltoldos.model.Categoria;
import com.ecommerce.fulltoldos.service.CategoriaService;
import com.ecommerce.fulltoldos.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private FileStorageService fileStorageService;

    // ── Listar todas ──
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("titulo", "Categorías");
        return "admin/categorias/lista";
    }

    // ── Formulario nueva categoría ──
    @GetMapping("/nueva")
    public String formNueva(Model model) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("titulo", "Nueva Categoría");
        model.addAttribute("accion", "nueva");
        return "admin/categorias/form";
    }

    // ── Formulario editar categoría ──
    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model,
                             RedirectAttributes flash) {
        return categoriaService.buscarPorId(id).map(cat -> {
            model.addAttribute("categoria", cat);
            model.addAttribute("titulo", "Editar Categoría");
            model.addAttribute("accion", "editar");
            return "admin/categorias/form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Categoría no encontrada");
            return "redirect:/admin/categorias";
        });
    }

    // ── Guardar (crear y editar) ──
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria,
                          @RequestParam(value = "imagenFile", required = false)
                          MultipartFile imagenFile,
                          RedirectAttributes flash) {
        try {
            // Si subió una imagen nueva
            if (imagenFile != null && !imagenFile.isEmpty()) {
                if (!fileStorageService.esImagenValida(imagenFile)) {
                    flash.addFlashAttribute("error", "Solo se permiten imágenes JPG, PNG o WEBP");
                    return "redirect:/admin/categorias";
                }
                // Eliminar imagen anterior si existe
                if (categoria.getImagenUrl() != null) {
                    fileStorageService.eliminarArchivo(categoria.getImagenUrl());
                }
                String url = fileStorageService.guardarArchivo(imagenFile, "categorias");
                categoria.setImagenUrl(url);
            }
            categoriaService.guardar(categoria);
            flash.addFlashAttribute("exito",
                    categoria.getId() == null ? "Categoría creada" : "Categoría actualizada");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/categorias";
    }

    // ── Cambiar estado activo/inactivo ──
    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam boolean activo,
                                RedirectAttributes flash) {
        categoriaService.cambiarEstado(id, activo);
        flash.addFlashAttribute("exito",
                activo ? "Categoría activada" : "Categoría desactivada");
        return "redirect:/admin/categorias";
    }

    // ── Eliminar ──
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            categoriaService.eliminar(id);
            flash.addFlashAttribute("exito", "Categoría eliminada");
        } catch (Exception e) {
            flash.addFlashAttribute("error",
                    "No se puede eliminar: tiene productos asociados");
        }
        return "redirect:/admin/categorias";
    }
}