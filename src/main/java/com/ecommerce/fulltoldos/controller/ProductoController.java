package com.ecommerce.fulltoldos.controller;


import com.ecommerce.fulltoldos.model.Producto;
import com.ecommerce.fulltoldos.service.CategoriaService;
import com.ecommerce.fulltoldos.service.ProductoService;
import com.ecommerce.fulltoldos.service.SubcategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private SubcategoriaService subcategoriaService;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina,
                         @RequestParam(defaultValue = "10") int tamanio,
                         Model model) {
        Pageable pageable = PageRequest.of(pagina, tamanio);
        model.addAttribute("productos", productoService.listarTodos(pageable));
        model.addAttribute("titulo", "Productos");
        model.addAttribute("paginaActual", pagina);
        return "admin/productos/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("subcategorias", subcategoriaService.listarTodas());
        model.addAttribute("titulo", "Nuevo Producto");
        model.addAttribute("accion", "nuevo");
        return "admin/productos/form";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model,
                             RedirectAttributes flash) {
        return productoService.buscarPorId(id).map(prod -> {
            model.addAttribute("producto", prod);
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("subcategorias", subcategoriaService.listarTodas());
            model.addAttribute("medias", productoService.listarMedia(id));
            model.addAttribute("titulo", "Editar Producto");
            model.addAttribute("accion", "editar");
            return "admin/productos/form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/admin/productos";
        });
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model,
                          RedirectAttributes flash) {
        return productoService.buscarPorId(id).map(prod -> {
            model.addAttribute("producto", prod);
            model.addAttribute("medias", productoService.listarMedia(id));
            model.addAttribute("titulo", prod.getNombre());
            return "admin/productos/detalle";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/admin/productos";
        });
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto,
                          @RequestParam("categoriaId") Long categoriaId,
                          @RequestParam(value = "subcategoriaId", required = false) Long subcategoriaId,
                          @RequestParam(value = "imagenes", required = false)
                          List<MultipartFile> imagenes,
                          @RequestParam(value = "urlVideo", required = false)
                          String urlVideo,
                          @RequestParam(value = "videoFile", required = false)
                          MultipartFile videoFile,
                          RedirectAttributes flash) {
        try {
            categoriaService.buscarPorId(categoriaId).ifPresent(producto::setCategoria);
            if (subcategoriaId != null) {
                subcategoriaService.buscarPorId(subcategoriaId).ifPresent(producto::setSubcategoria);
            } else {
                producto.setSubcategoria(null);
            }

            if (producto.getPrecio() == null) {
                producto.setPrecio(BigDecimal.ZERO);
            }

            Producto guardado = productoService.guardar(producto);

            if (imagenes != null) {
                boolean esPrimera = productoService.listarMedia(guardado.getId()).isEmpty();
                for (MultipartFile img : imagenes) {
                    if (!img.isEmpty()) {
                        productoService.agregarImagen(guardado.getId(), img, esPrimera);
                        esPrimera = false;
                    }
                }
            }

            if (urlVideo != null && !urlVideo.trim().isEmpty()) {
                productoService.agregarVideo(guardado.getId(), urlVideo.trim());
            }

            if (videoFile != null && !videoFile.isEmpty()) {
                productoService.agregarVideoLocal(guardado.getId(), videoFile);
            }

            flash.addFlashAttribute("exito",
                    producto.getId() == null ? "Producto creado" : "Producto actualizado");
            return "redirect:/admin/productos/editar/" + guardado.getId();

        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
            if (producto.getId() != null) {
                return "redirect:/admin/productos/editar/" + producto.getId();
            }
            return "redirect:/admin/productos";
        }
    }

    @PostMapping("/{id}/imagen")
    public String agregarImagen(@PathVariable Long id,
                                @RequestParam("imagen") MultipartFile imagen,
                                @RequestParam(value = "principal",
                                        defaultValue = "false") boolean principal,
                                RedirectAttributes flash) {
        try {
            productoService.agregarImagen(id, imagen, principal);
            flash.addFlashAttribute("exito", "Imagen agregada");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/productos/editar/" + id;
    }

    @PostMapping("/{id}/video")
    public String agregarVideo(@PathVariable Long id,
                               @RequestParam("urlVideo") String urlVideo,
                               RedirectAttributes flash) {
        try {
            productoService.agregarVideo(id, urlVideo.trim());
            flash.addFlashAttribute("exito", "Video de YouTube agregado");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/productos/editar/" + id;
    }

    @PostMapping("/{id}/video-local")
    public String agregarVideoLocal(@PathVariable Long id,
                                    @RequestParam("videoFile") MultipartFile videoFile,
                                    RedirectAttributes flash) {
        try {
            productoService.agregarVideoLocal(id, videoFile);
            flash.addFlashAttribute("exito", "Video subido correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/productos/editar/" + id;
    }

    @PostMapping("/media/{mediaId}/principal")
    public String marcarPrincipal(@PathVariable Long mediaId,
                                  @RequestParam Long productoId,
                                  RedirectAttributes flash) {
        productoService.marcarPrincipal(mediaId);
        flash.addFlashAttribute("exito", "Imagen principal actualizada");
        return "redirect:/admin/productos/editar/" + productoId;
    }

    @PostMapping("/media/{mediaId}/eliminar")
    public String eliminarMedia(@PathVariable Long mediaId,
                                @RequestParam Long productoId,
                                RedirectAttributes flash) {
        productoService.eliminarMedia(mediaId);
        flash.addFlashAttribute("exito", "Archivo eliminado");
        return "redirect:/admin/productos/editar/" + productoId;
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam boolean activo,
                                RedirectAttributes flash) {
        productoService.cambiarEstado(id, activo);
        flash.addFlashAttribute("exito",
                activo ? "Producto activado" : "Producto desactivado");
        return "redirect:/admin/productos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            productoService.eliminar(id);
            flash.addFlashAttribute("exito", "Producto eliminado");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/admin/productos";
    }
}
