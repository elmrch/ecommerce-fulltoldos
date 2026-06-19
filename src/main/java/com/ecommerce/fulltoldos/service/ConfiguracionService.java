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
        insertar("cotizacion.email",     "elmrchahua8@gmail.com",   "Correo que recibe las cotizaciones",      "cotizacion");
        insertar("cotizacion.whatsapp",  "51999999999",              "Número WhatsApp (con código de país)",    "cotizacion");
        insertar("topbar.telefono",      "+51 999 999 999",          "Teléfono visible en la barra superior",   "contacto");
        insertar("topbar.horario",       "Lun–Sáb 8am–7pm",         "Horario visible en la barra superior",    "contacto");
        insertar("topbar.envios",        "Envíos a todo el Perú",    "Texto de envíos en la barra superior",    "contacto");
        insertar("redes.whatsapp",       "https://wa.me/51999999999","Enlace de WhatsApp",                      "redes");
        insertar("redes.facebook",       "#",                        "Enlace de Facebook",                      "redes");
        insertar("redes.instagram",      "#",                        "Enlace de Instagram",                     "redes");
        insertar("redes.tiktok",         "#",                        "Enlace de TikTok",                        "redes");
        insertar("redes.youtube",        "#",                        "Enlace de YouTube",                       "redes");
        insertar("empresa.ruc",          "20XXXXXXXXX",              "RUC de la empresa",                       "empresa");
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
