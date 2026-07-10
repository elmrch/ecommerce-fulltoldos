package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.NosotrosBloque;
import com.ecommerce.fulltoldos.service.ConfiguracionService;
import com.ecommerce.fulltoldos.service.NosotrosBloqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/nosotros")
public class NosotrosAdminController {

    @Autowired private NosotrosBloqueService bloqueService;
    @Autowired private ConfiguracionService configService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("bloques", bloqueService.listarTodos());

        model.addAttribute("faqTag",     configService.get("nosotros.faq.tag",     "Preguntas frecuentes"));
        model.addAttribute("faqHeading", configService.get("nosotros.faq.heading", "¿Tienes alguna pregunta? Estamos aquí para ayudarte."));
        model.addAttribute("horarioTexto", configService.get("nosotros.horario.texto", "Horario de atención: lunes a sábado, de 8:00 a.m. a 7:00 p.m."));
        model.addAttribute("horarioNota",  configService.get("nosotros.horario.nota",  "(Excepto feriados)"));
        model.addAttribute("socialTitulo", configService.get("nosotros.social.titulo", "Síguenos en redes sociales"));

        model.addAttribute("stat1Numero", configService.get("nosotros.stat1.numero", "25"));
        model.addAttribute("stat1Label",  configService.get("nosotros.stat1.label",  "25 AÑOS DE EXPERIENCIA"));
        model.addAttribute("stat1Desc",   configService.get("nosotros.stat1.desc",   "— la columna vertebral de la misión de Full Toldos para ofrecer coberturas de calidad en todo el Perú."));
        model.addAttribute("stat2Numero", configService.get("nosotros.stat2.numero", "48"));
        model.addAttribute("stat2Label",  configService.get("nosotros.stat2.label",  "48 HORAS"));
        model.addAttribute("stat2Desc",   configService.get("nosotros.stat2.desc",   "— el tiempo promedio de fabricación para garantizar entrega puntual y calidad en cada pedido."));
        model.addAttribute("stat3Numero", configService.get("nosotros.stat3.numero", "5K+"));
        model.addAttribute("stat3Label",  configService.get("nosotros.stat3.label",  "más de 5.000 clientes"));
        model.addAttribute("stat3Desc",   configService.get("nosotros.stat3.desc",   "— ya confían en Full Toldos para proteger sus negocios, hogares y eventos en todo el Perú."));

        return "admin/nosotros/lista";
    }

    @PostMapping("/config")
    public String guardarConfig(@RequestParam java.util.Map<String, String> params, RedirectAttributes flash) {
        params.forEach((clave, valor) -> {
            if (clave.startsWith("nosotros.")) configService.guardar(clave, valor);
        });
        flash.addFlashAttribute("ok", "Sección de Nosotros actualizada.");
        return "redirect:/admin/nosotros";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("bloque", new NosotrosBloque());
        model.addAttribute("titulo", "Nuevo Bloque");
        return "admin/nosotros/bloque-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return bloqueService.buscarPorId(id).map(b -> {
            model.addAttribute("bloque", b);
            model.addAttribute("titulo", "Editar Bloque");
            return "admin/nosotros/bloque-form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Bloque no encontrado");
            return "redirect:/admin/nosotros";
        });
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute NosotrosBloque bloque,
                           @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                           @RequestParam(value = "activo", required = false) String activoParam,
                           RedirectAttributes flash) {
        try {
            bloque.setActivo("true".equals(activoParam));
            bloqueService.guardar(bloque, imagen);
            flash.addFlashAttribute("ok", bloque.getId() == null ? "Bloque creado" : "Bloque actualizado");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/nosotros";
    }

    @PostMapping("/{id}/activo")
    public String activo(@PathVariable Long id, @RequestParam boolean activo, RedirectAttributes flash) {
        bloqueService.cambiarActivo(id, activo);
        flash.addFlashAttribute("ok", activo ? "Bloque activado" : "Bloque desactivado");
        return "redirect:/admin/nosotros";
    }

    @PostMapping("/{id}/subir")
    public String subir(@PathVariable Long id) {
        bloqueService.reordenar(id, -1);
        return "redirect:/admin/nosotros";
    }

    @PostMapping("/{id}/bajar")
    public String bajar(@PathVariable Long id) {
        bloqueService.reordenar(id, 1);
        return "redirect:/admin/nosotros";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        bloqueService.eliminar(id);
        flash.addFlashAttribute("ok", "Bloque eliminado");
        return "redirect:/admin/nosotros";
    }
}
