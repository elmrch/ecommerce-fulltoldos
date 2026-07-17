package com.ecommerce.fulltoldos.service;

import com.ecommerce.fulltoldos.model.Configuracion;
import com.ecommerce.fulltoldos.repository.ConfiguracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfiguracionService {

    @Autowired
    private ConfiguracionRepository repo;

    public String get(String clave, String defaultVal) {
        return repo.findById(clave).map(Configuracion::getValor).orElse(defaultVal);
    }

    public List<Configuracion> porGrupo(String grupo) {
        return repo.findByGrupoOrderByClave(grupo);
    }

    public List<Configuracion> todos() {
        return repo.findAll();
    }

    public void guardar(String clave, String valor) {
        Configuracion c = repo.findById(clave).orElseGet(() -> {
            Configuracion n = new Configuracion();
            n.setClave(clave);
            return n;
        });
        c.setValor(valor);
        repo.save(c);
    }

    public void guardarTodos(List<Configuracion> lista) {
        repo.saveAll(lista);
    }

    /** Inserta valores por defecto si la clave no existe todavía */
    public void initDefaults() {
        insertar("cotizacion.email",     "ventas@fulltoldos.com",   "Correo que recibe las cotizaciones",      "cotizacion");
        insertar("cotizacion.whatsapp",  "51978391237",              "Número WhatsApp (con código de país)",    "cotizacion");
        insertar("topbar.telefono",      "+51 978 391 237",          "Teléfono visible en la barra superior",   "contacto");
        insertar("topbar.horario",       "Lun–Sáb 8am–7pm",         "Horario visible en la barra superior",    "contacto");
        insertar("topbar.envios",        "Envíos a todo el Perú",    "Texto de envíos en la barra superior",    "contacto");
        insertar("contacto.email",       "ventas@fulltoldos.com",   "Correo que recibe suscripciones al newsletter y mensajes del formulario de contacto", "contacto");
        insertar("topbar.color.fondo",   "#b9f6ca",                  "Color de fondo de la barra superior",     "contacto");
        insertar("topbar.fuente",        "'Poppins', sans-serif",    "Fuente de la barra superior (CSS font-family)", "contacto");
        insertar("topbar.color.fuente",  "#111111",                  "Color de letra de la barra superior",     "contacto");
        insertar("sitio.logo",           "/logofulltoldos.png",      "Logo del sitio (barra de navegación)",    "logo");
        insertar("sitio.logo.posicion",  "left",                     "Posición del logo en la barra: left, center o right", "logo");

        // Newsletter (sección del home)
        insertar("newsletter.titulo",        "Suscríbete a Nuestro Newsletter",                              "Título de la sección",                    "newsletter");
        insertar("newsletter.descripcion",   "Novedades, ofertas exclusivas y consejos sobre toldos y carpas.", "Descripción de la sección",            "newsletter");
        insertar("newsletter.placeholder",   "Ingresa tu correo electrónico",                                "Placeholder del campo de correo",         "newsletter");
        insertar("newsletter.boton",         "SUSCRIBIRME",                                                   "Texto del botón",                         "newsletter");
        insertar("newsletter.nota",          "No te pierdas lo mejor en toldos y carpas.",                   "Texto pequeño debajo del formulario",      "newsletter");
        insertar("newsletter.color.fondo",   "#2a44d6",                                                       "Color de fondo de la sección (hex)",      "newsletter");
        insertar("newsletter.imagen",        "/img/imgfooter.png",                                            "Imagen lateral de la sección",            "newsletter");
        insertar("newsletter.email.asunto",  "Nueva suscripción al Newsletter — Full Toldos",                "Asunto del correo que te llega. Puedes usar {email}", "newsletter");
        insertar("newsletter.email.cuerpo",  "Nuevo suscriptor al newsletter.\n\nCorreo: {email}",           "Cuerpo del correo. Usa {email} para el correo del suscriptor", "newsletter");

        // Formulario de contacto (página de contacto)
        insertar("contacto.form.tag",         "Contáctenos",                    "Etiqueta pequeña sobre el título",        "contacto_form");
        insertar("contacto.form.titulo",      "¡Estamos aquí para ayudarte!",   "Título de la página de contacto",         "contacto_form");
        insertar("contacto.form.descripcion", "Si tienes preguntas sobre tu pedido, necesitas ayuda con nuestros productos o simplemente deseas comunicarte con nosotros, no dudes en escribirnos.", "Descripción de la página de contacto", "contacto_form");
        insertar("contacto.email.asunto",     "Nuevo mensaje de contacto de {nombre} — Full Toldos", "Asunto del correo. Puedes usar {nombre}", "contacto_form");
        insertar("contacto.email.cuerpo",     "Nombre: {nombre}\nCorreo: {email}\n\nMensaje:\n{mensaje}", "Cuerpo del correo. Usa {nombre}, {email} y {mensaje}", "contacto_form");

        insertar("redes.whatsapp",       "https://wa.me/51978391237","Enlace de WhatsApp",                      "redes");
        insertar("redes.facebook",       "#",                        "Enlace de Facebook",                      "redes");
        insertar("redes.instagram",      "#",                        "Enlace de Instagram",                     "redes");
        insertar("redes.tiktok",         "#",                        "Enlace de TikTok",                        "redes");
        insertar("redes.youtube",        "#",                        "Enlace de YouTube",                       "redes");
        insertar("empresa.ruc",          "20XXXXXXXXX",              "RUC de la empresa",                       "empresa");
        insertar("banner.central.imagen",    "/img/fulltoldos.png",  "Imagen del banner central (debajo de productos)", "banner");
        insertar("banner.central.link.tipo", "whatsapp",             "Tipo de link del banner central",                 "banner");
        insertar("banner.central.link.valor","",                     "Valor del link del banner central",               "banner");
        insertar("pago.culqi.public_key", "", "Llave pública de Culqi (checkout.js) — actívala aquí cuando la tengas", "pago");
        insertar("pago.culqi.secret_key", "", "Llave secreta de Culqi (para cargos) — no la compartas",                "pago");
        insertar("pago.envio.costo",         "15",  "Costo de envío estándar (S/)",                         "pago");
        insertar("pago.envio.gratis_desde",  "300", "Envío gratis para pedidos desde este monto (S/)",      "pago");
    }

    private void insertar(String clave, String valorDefault, String descripcion, String grupo) {
        if (!repo.existsById(clave)) {
            Configuracion c = new Configuracion();
            c.setClave(clave);
            c.setValor(valorDefault);
            c.setDescripcion(descripcion);
            c.setGrupo(grupo);
            repo.save(c);
        }
    }
}
