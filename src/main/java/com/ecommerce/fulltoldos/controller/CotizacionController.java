package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.Cotizacion;
import com.ecommerce.fulltoldos.repository.CotizacionRepository;
import com.ecommerce.fulltoldos.service.ConfiguracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class CotizacionController {

    @Autowired
    private CotizacionRepository cotizacionRepository;

    @Autowired
    private ConfiguracionService configService;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @PostMapping("/cotizar")
    public ResponseEntity<Map<String, String>> cotizar(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String correo,
            @RequestParam String telefono,
            @RequestParam(defaultValue = "") String empresa,
            @RequestParam(defaultValue = "") String tipoToldo,
            @RequestParam(defaultValue = "") String medidas,
            @RequestParam(defaultValue = "") String mensaje,
            @RequestParam(defaultValue = "") String comoSentero
    ) {
        Cotizacion cot = new Cotizacion();
        cot.setNombre(nombre);
        cot.setApellido(apellido);
        cot.setCorreo(correo);
        cot.setTelefono(telefono);
        cot.setEmpresa(empresa);
        cot.setTipoToldo(tipoToldo);
        cot.setMedidas(medidas);
        cot.setMensaje(mensaje);
        cot.setComoSentero(comoSentero);
        cotizacionRepository.save(cot);

        // Leer desde la base de datos (configurado en el dashboard)
        String emailDestino = configService.get("cotizacion.email", "elmrchahua8@gmail.com");
        String whatsappNum  = configService.get("cotizacion.whatsapp", "51999999999");

        if (mailSender != null) {
            try {
                String cuerpo = String.format(
                    "NUEVA SOLICITUD DE COTIZACIÓN — FULL TOLDOS%n" +
                    "============================================%n" +
                    "Nombre:    %s %s%n" +
                    "Correo:    %s%n" +
                    "Teléfono:  %s%n" +
                    "Empresa:   %s%n" +
                    "Tipo:      %s%n" +
                    "Medidas:   %s%n%n" +
                    "Descripción:%n%s%n%n" +
                    "¿Cómo se enteró?: %s",
                    nombre, apellido, correo, telefono,
                    empresa, tipoToldo, medidas, mensaje, comoSentero
                );
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setTo(emailDestino);
                mail.setReplyTo(correo);
                mail.setSubject("Cotización de " + nombre + " " + apellido + " — Full Toldos");
                mail.setText(cuerpo);
                mailSender.send(mail);
            } catch (Exception ignored) {}
        }

        String wspMsg = "Hola Full Toldos! Solicito cotización:\n" +
                "• Nombre: " + nombre + " " + apellido + "\n" +
                "• Teléfono: " + telefono + "\n" +
                "• Tipo: " + tipoToldo + "\n" +
                "• Medidas: " + medidas + "\n" +
                "• Detalles: " + mensaje;
        String wspUrl = "https://wa.me/" + whatsappNum +
                "?text=" + URLEncoder.encode(wspMsg, StandardCharsets.UTF_8);

        return ResponseEntity.ok(Map.of("status", "ok", "wspUrl", wspUrl));
    }
}
