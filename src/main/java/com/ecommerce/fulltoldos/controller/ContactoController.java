package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.service.ConfiguracionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ContactoController {

    private static final Logger log = LoggerFactory.getLogger(ContactoController.class);

    @Autowired
    private ConfiguracionService configService;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @PostMapping("/newsletter/suscribir")
    public ResponseEntity<Map<String, String>> suscribir(@RequestParam String email) {
        String asunto = configService.get("newsletter.email.asunto", "Nueva suscripción al Newsletter — Full Toldos")
                .replace("{email}", email);
        String cuerpo = configService.get("newsletter.email.cuerpo", "Nuevo suscriptor al newsletter.\n\nCorreo: {email}")
                .replace("{email}", email);
        enviarNotificacion(asunto, cuerpo, null);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/contacto/enviar")
    public ResponseEntity<Map<String, String>> enviarContacto(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String mensaje
    ) {
        String asunto = configService.get("contacto.email.asunto", "Nuevo mensaje de contacto de {nombre} — Full Toldos")
                .replace("{nombre}", nombre);
        String cuerpo = configService.get("contacto.email.cuerpo", "Nombre: {nombre}\nCorreo: {email}\n\nMensaje:\n{mensaje}")
                .replace("{nombre}", nombre)
                .replace("{email}", email)
                .replace("{mensaje}", mensaje);
        enviarNotificacion(asunto, cuerpo, email);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    private void enviarNotificacion(String asunto, String cuerpo, String replyTo) {
        if (mailSender == null) {
            log.warn("No se pudo enviar el correo '{}': no hay JavaMailSender configurado (falta spring.mail.* en application.properties)", asunto);
            return;
        }
        try {
            String emailDestino = configService.get("contacto.email", "elmrchahua8@gmail.com");
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(emailDestino);
            if (replyTo != null && !replyTo.isBlank()) {
                mail.setReplyTo(replyTo);
            }
            mail.setSubject(asunto);
            mail.setText(cuerpo);
            mailSender.send(mail);
        } catch (Exception e) {
            log.error("Error enviando correo '{}'", asunto, e);
        }
    }
}
