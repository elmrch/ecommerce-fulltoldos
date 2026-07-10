package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.service.CategoriaService;
import com.ecommerce.fulltoldos.service.ProductoService;
import com.ecommerce.fulltoldos.util.SlugUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Genera /sitemap.xml con las páginas públicas del sitio (inicio, envíos, categorías y productos activos). */
@RestController
public class SitemapController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap(HttpServletRequest request) {
        String base = request.getScheme() + "://" + request.getServerName()
                + (isDefaultPort(request) ? "" : ":" + request.getServerPort());

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        agregarUrl(xml, base + "/");
        agregarUrl(xml, base + "/envios");
        agregarUrl(xml, base + "/contacto");
        agregarUrl(xml, base + "/sobre-nosotros");

        categoriaService.listarActivas().forEach(c ->
                agregarUrl(xml, base + "/categoria/" + SlugUtil.toSlug(c.getNombre())));

        productoService.listarActivos().forEach(p ->
                agregarUrl(xml, base + "/producto/" + SlugUtil.toSlug(p.getNombre())));

        xml.append("</urlset>\n");
        return xml.toString();
    }

    private boolean isDefaultPort(HttpServletRequest request) {
        int port = request.getServerPort();
        return ("http".equals(request.getScheme()) && port == 80)
                || ("https".equals(request.getScheme()) && port == 443);
    }

    private void agregarUrl(StringBuilder xml, String loc) {
        xml.append("  <url><loc>").append(loc).append("</loc></url>\n");
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String robots(HttpServletRequest request) {
        String base = request.getScheme() + "://" + request.getServerName()
                + (isDefaultPort(request) ? "" : ":" + request.getServerPort());
        return "User-agent: *\n"
                + "Allow: /\n"
                + "Disallow: /admin/\n"
                + "Disallow: /cliente/\n"
                + "\n"
                + "Sitemap: " + base + "/sitemap.xml\n";
    }
}
