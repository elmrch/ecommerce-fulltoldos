package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.EnvioAgencia;
import com.ecommerce.fulltoldos.model.EnvioHistorial;
import com.ecommerce.fulltoldos.service.ConfiguracionService;
import com.ecommerce.fulltoldos.service.EnvioAgenciaService;
import com.ecommerce.fulltoldos.service.EnvioHistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/envios")
public class EnvioAdminController {

    @Autowired private EnvioHistorialService historialService;
    @Autowired private EnvioAgenciaService agenciaService;
    @Autowired private ConfiguracionService configService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("historial", historialService.listarTodos());
        model.addAttribute("agencias", agenciaService.listarTodos());

        model.addAttribute("heroBadge",      configService.get("envios.hero.badge",      "MONITOREO DE LOGÍSTICA EN VIVO"));
        model.addAttribute("heroTitulo1",    configService.get("envios.hero.titulo1",    "Enviamos tecnología"));
        model.addAttribute("heroTitulo2",    configService.get("envios.hero.titulo2",    "a todo el"));
        model.addAttribute("heroDestacado",  configService.get("envios.hero.destacado",  "Perú"));
        model.addAttribute("heroSubtitulo",  configService.get("envios.hero.subtitulo",  "Nuestra prioridad es que tu pedido llegue seguro. Gestionamos cada envío con agencias reconocidas del país."));

        model.addAttribute("stat1Numero", configService.get("envios.stat1.numero", "100%"));
        model.addAttribute("stat1Label",  configService.get("envios.stat1.label",  "GARANTIZADO"));
        model.addAttribute("stat2Numero", configService.get("envios.stat2.numero", "24-73h"));
        model.addAttribute("stat2Label",  configService.get("envios.stat2.label",  "TIEMPO ENTREGA"));
        model.addAttribute("stat3Numero", configService.get("envios.stat3.numero", "24"));
        model.addAttribute("stat3Label",  configService.get("envios.stat3.label",  "REGIONES"));
        model.addAttribute("stat4Numero", configService.get("envios.stat4.numero", "+1,000"));
        model.addAttribute("stat4Label",  configService.get("envios.stat4.label",  "ENVÍOS EXITOSOS"));

        model.addAttribute("histTitulo",    configService.get("envios.hist.titulo",    "Historial de Envíos Realizados"));
        model.addAttribute("histSubtitulo", configService.get("envios.hist.subtitulo", "Bitácora de entregas procesadas y finalizadas con éxito."));

        model.addAttribute("s3Titulo",       configService.get("envios.s3.titulo",       "Envíos a todo el Perú por tu agencia favorita"));
        model.addAttribute("s3Descripcion",  configService.get("envios.s3.descripcion",  "Gestionamos tus pedidos a través de los operadores logísticos más importantes del país. Tú eliges la agencia que más te convenga."));
        model.addAttribute("s3Nota",         configService.get("envios.s3.nota",         "Confirmación de entrega vía WhatsApp."));
        model.addAttribute("s3Btn1",         configService.get("envios.s3.btn1",         "Coordina tu envío"));
        model.addAttribute("s3Btn2",         configService.get("envios.s3.btn2",         "Ver historial"));
        model.addAttribute("s3Nota2",        configService.get("envios.s3.nota2",        "*Otras empresas de transporte disponibles previa coordinación."));

        return "admin/envios/lista";
    }

    @PostMapping("/config")
    public String guardarConfig(@RequestParam java.util.Map<String, String> params, RedirectAttributes flash) {
        params.forEach((clave, valor) -> {
            if (clave.startsWith("envios.")) configService.guardar(clave, valor);
        });
        flash.addFlashAttribute("ok", "Sección de envíos actualizada.");
        return "redirect:/admin/envios";
    }

    /* ── Historial ── */

    @GetMapping("/historial/nuevo")
    public String historialNuevo(Model model) {
        model.addAttribute("envio", new EnvioHistorial());
        model.addAttribute("titulo", "Nuevo Envío del Historial");
        return "admin/envios/historial-form";
    }

    @GetMapping("/historial/editar/{id}")
    public String historialEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return historialService.buscarPorId(id).map(e -> {
            model.addAttribute("envio", e);
            model.addAttribute("titulo", "Editar Envío del Historial");
            return "admin/envios/historial-form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Registro no encontrado");
            return "redirect:/admin/envios";
        });
    }

    @PostMapping("/historial/guardar")
    public String historialGuardar(@ModelAttribute EnvioHistorial envio,
                                    @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                    @RequestParam(value = "activo", required = false) String activoParam,
                                    RedirectAttributes flash) {
        try {
            envio.setActivo("true".equals(activoParam));
            historialService.guardar(envio, imagen);
            flash.addFlashAttribute("ok", envio.getId() == null ? "Registro creado" : "Registro actualizado");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/envios";
    }

    @PostMapping("/historial/{id}/activo")
    public String historialActivo(@PathVariable Long id, @RequestParam boolean activo, RedirectAttributes flash) {
        historialService.cambiarActivo(id, activo);
        flash.addFlashAttribute("ok", activo ? "Registro activado" : "Registro desactivado");
        return "redirect:/admin/envios";
    }

    @PostMapping("/historial/{id}/subir")
    public String historialSubir(@PathVariable Long id) {
        historialService.reordenar(id, -1);
        return "redirect:/admin/envios";
    }

    @PostMapping("/historial/{id}/bajar")
    public String historialBajar(@PathVariable Long id) {
        historialService.reordenar(id, 1);
        return "redirect:/admin/envios";
    }

    @PostMapping("/historial/{id}/eliminar")
    public String historialEliminar(@PathVariable Long id, RedirectAttributes flash) {
        historialService.eliminar(id);
        flash.addFlashAttribute("ok", "Registro eliminado");
        return "redirect:/admin/envios";
    }

    /* ── Agencias ── */

    @GetMapping("/agencias/nuevo")
    public String agenciaNueva(Model model) {
        model.addAttribute("agencia", new EnvioAgencia());
        model.addAttribute("titulo", "Nueva Agencia");
        return "admin/envios/agencia-form";
    }

    @GetMapping("/agencias/editar/{id}")
    public String agenciaEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return agenciaService.buscarPorId(id).map(a -> {
            model.addAttribute("agencia", a);
            model.addAttribute("titulo", "Editar Agencia");
            return "admin/envios/agencia-form";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "Agencia no encontrada");
            return "redirect:/admin/envios";
        });
    }

    @PostMapping("/agencias/guardar")
    public String agenciaGuardar(@ModelAttribute EnvioAgencia agencia,
                                  @RequestParam(value = "logo", required = false) MultipartFile logo,
                                  @RequestParam(value = "activo", required = false) String activoParam,
                                  RedirectAttributes flash) {
        try {
            agencia.setActivo("true".equals(activoParam));
            agenciaService.guardar(agencia, logo);
            flash.addFlashAttribute("ok", agencia.getId() == null ? "Agencia creada" : "Agencia actualizada");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/envios";
    }

    @PostMapping("/agencias/{id}/activo")
    public String agenciaActivo(@PathVariable Long id, @RequestParam boolean activo, RedirectAttributes flash) {
        agenciaService.cambiarActivo(id, activo);
        flash.addFlashAttribute("ok", activo ? "Agencia activada" : "Agencia desactivada");
        return "redirect:/admin/envios";
    }

    @PostMapping("/agencias/{id}/subir")
    public String agenciaSubir(@PathVariable Long id) {
        agenciaService.reordenar(id, -1);
        return "redirect:/admin/envios";
    }

    @PostMapping("/agencias/{id}/bajar")
    public String agenciaBajar(@PathVariable Long id) {
        agenciaService.reordenar(id, 1);
        return "redirect:/admin/envios";
    }

    @PostMapping("/agencias/{id}/eliminar")
    public String agenciaEliminar(@PathVariable Long id, RedirectAttributes flash) {
        agenciaService.eliminar(id);
        flash.addFlashAttribute("ok", "Agencia eliminada");
        return "redirect:/admin/envios";
    }
}
