package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.service.CategoriaService;
import com.ecommerce.fulltoldos.service.ProductoService;
import com.ecommerce.fulltoldos.util.SlugUtil;
import com.ecommerce.fulltoldos.util.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Genera /sitemap.xml con las páginas públicas del sitio (inicio, envíos, categorías y productos activos). */
@RestController
public class SitemapController {


    private static final DateTimeFormatter LASTMOD = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap(HttpServletRequest request) {
        String base = UrlUtil.baseUrl(request);

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        agregarUrl(xml, base + "/", null, "daily", "1.0");
        agregarUrl(xml, base + "/ofertas", null, "daily", "0.9");
        agregarUrl(xml, base + "/cotizar", null, "monthly", "0.8");
        agregarUrl(xml, base + "/envios", null, "monthly", "0.5");
        agregarUrl(xml, base + "/contacto", null, "monthly", "0.5");
        agregarUrl(xml, base + "/sobre-nosotros", null, "monthly", "0.5");
        agregarUrl(xml, base + "/politica-de-privacidad", null, "yearly", "0.2");
        agregarUrl(xml, base + "/terminos-y-condiciones", null, "yearly", "0.2");
        agregarUrl(xml, base + "/cambios-y-devoluciones", null, "yearly", "0.2");

        categoriaService.listarActivas().forEach(c ->
                agregarUrl(xml, base + "/categoria/" + SlugUtil.toSlug(c.getNombre()),
                        c.getFechaModificacion(), "weekly", "0.8"));

        productoService.listarActivos().forEach(p ->
                agregarUrl(xml, base + "/producto/" + SlugUtil.toSlug(p.getNombre()),
                        p.getFechaModificacion(), "weekly", "0.7"));

        xml.append("</urlset>\n");
        return xml.toString();
    }

    private void agregarUrl(StringBuilder xml, String loc, LocalDateTime lastmod, String changefreq, String priority) {
        xml.append("  <url><loc>").append(loc).append("</loc>");
        if (lastmod != null) {
            xml.append("<lastmod>").append(lastmod.format(LASTMOD)).append("</lastmod>");
        }
        xml.append("<changefreq>").append(changefreq).append("</changefreq>");
        xml.append("<priority>").append(priority).append("</priority>");
        xml.append("</url>\n");
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String robots(HttpServletRequest request) {
        String base = UrlUtil.baseUrl(request);
        return "User-agent: *\n"
                + "Allow: /\n"
                + "Disallow: /admin/\n"
                + "Disallow: /cliente/\n"
                + "\n"
                + "Sitemap: " + base + "/sitemap.xml\n";
    }
}
