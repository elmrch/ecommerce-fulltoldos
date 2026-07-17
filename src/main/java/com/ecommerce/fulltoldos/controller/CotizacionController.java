package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.Cotizacion;
import com.ecommerce.fulltoldos.repository.CotizacionRepository;
import com.ecommerce.fulltoldos.service.ConfiguracionService;
import com.ecommerce.fulltoldos.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CotizacionController {

    private static final Logger log = LoggerFactory.getLogger(CotizacionController.class);
    private static final int MAX_IMAGENES = 5;

    @Autowired
    private CotizacionRepository cotizacionRepository;

    @Autowired
    private ConfiguracionService configService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @PostMapping("/cotizar")
    public ResponseEntity<Map<String, String>> cotizar(
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String celular,
            @RequestParam(defaultValue = "") String empresa,
            @RequestParam String tipoProducto,
            @RequestParam(defaultValue = "") String ancho,
            @RequestParam(defaultValue = "") String largo,
            @RequestParam(defaultValue = "") String altura,
            @RequestParam(defaultValue = "") String comentarios,
            @RequestParam(value = "imagenes", required = false) List<MultipartFile> imagenes
    ) {
        Cotizacion cot = new Cotizacion();
        cot.setNombre(nombre);
        cot.setCorreo(correo);
        cot.setCelular(celular);
        cot.setEmpresa(empresa);
        cot.setTipoProducto(tipoProducto);
        cot.setAncho(ancho);
        cot.setLargo(largo);
        cot.setAltura(altura);
        cot.setComentarios(comentarios);

        // Guardar imágenes adjuntas (máximo MAX_IMAGENES, solo imágenes válidas)
        List<String> urls = new ArrayList<>();
        if (imagenes != null) {
            for (MultipartFile img : imagenes) {
                if (urls.size() >= MAX_IMAGENES) break;
                if (img == null || img.isEmpty()) continue;
                if (!fileStorageService.esImagenValida(img)) continue;
                urls.add(fileStorageService.guardarArchivo(img, "cotizaciones"));
            }
        }
        cot.setImagenes(String.join(",", urls));

        cotizacionRepository.save(cot);

        // Leer desde la base de datos (configurado en el dashboard)
        String emailDestino = configService.get("cotizacion.email", "ventas@fulltoldos.com");
        String whatsappNum  = configService.get("cotizacion.whatsapp", "51978391237");

        if (mailSender != null) {
            try {
                String medidas = cot.getMedidasTexto();
                String cuerpo = String.format(
                    "NUEVA SOLICITUD DE COTIZACIÓN — FULL TOLDOS%n" +
                    "============================================%n" +
                    "Nombre:      %s%n" +
                    "Correo:      %s%n" +
                    "Celular:     %s%n" +
                    "Empresa:     %s%n" +
                    "Tipo:        %s%n" +
                    "Medidas:     %s%n" +
                    "Imágenes:    %s%n%n" +
                    "Comentarios:%n%s",
                    nombre, correo, celular, empresa, tipoProducto, medidas,
                    urls.isEmpty() ? "(sin imágenes)" : urls.size() + " adjunta(s)",
                    comentarios
                );
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setTo(emailDestino);
                mail.setReplyTo(correo);
                mail.setSubject("Cotización de " + nombre + " — Full Toldos");
                mail.setText(cuerpo);
                mailSender.send(mail);
            } catch (Exception e) {
                log.error("Error enviando correo de cotización", e);
            }
        }

        String wspMsg = "Hola Full Toldos! Solicito cotización:\n" +
                "• Nombre: " + nombre + "\n" +
                "• Celular: " + celular + "\n" +
                "• Tipo: " + tipoProducto + "\n" +
                "• Medidas: " + cot.getMedidasTexto() + "\n" +
                "• Detalles: " + comentarios;
        String wspUrl = "https://wa.me/" + whatsappNum +
                "?text=" + URLEncoder.encode(wspMsg, StandardCharsets.UTF_8);

        return ResponseEntity.ok(Map.of("status", "ok", "wspUrl", wspUrl));
    }
}
