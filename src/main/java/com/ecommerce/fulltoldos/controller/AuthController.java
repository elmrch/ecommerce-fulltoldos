package com.ecommerce.fulltoldos.controller;
import com.ecommerce.fulltoldos.model.Categoria;
import com.ecommerce.fulltoldos.model.Producto;
import com.ecommerce.fulltoldos.repository.UsuarioRepository;
import com.ecommerce.fulltoldos.service.CategoriaService;
import com.ecommerce.fulltoldos.service.ConfiguracionService;
import com.ecommerce.fulltoldos.service.EnvioAgenciaService;
import com.ecommerce.fulltoldos.service.EnvioHistorialService;
import com.ecommerce.fulltoldos.service.NosotrosBloqueService;
import com.ecommerce.fulltoldos.service.PreguntaFrecuenteService;
import com.ecommerce.fulltoldos.service.ProductoCatalogoMapper;
import com.ecommerce.fulltoldos.service.ProductoService;
import com.ecommerce.fulltoldos.service.PromoBloqueService;
import com.ecommerce.fulltoldos.service.SlideService;
import com.ecommerce.fulltoldos.service.TestimonioService;
import com.ecommerce.fulltoldos.service.UsuarioService;
import com.ecommerce.fulltoldos.util.SlugUtil;
import com.ecommerce.fulltoldos.util.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired

    private CategoriaService categoriaService;
    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoCatalogoMapper productoCatalogoMapper;

    @Autowired
    private ConfiguracionService configService;

    @Autowired
    private SlideService slideService;

    @Autowired
    private PromoBloqueService promoBloqueService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnvioHistorialService envioHistorialService;

    @Autowired
    private EnvioAgenciaService envioAgenciaService;

    @Autowired
    private NosotrosBloqueService nosotrosBloqueService;

    @Autowired
    private PreguntaFrecuenteService preguntaFrecuenteService;

    @Autowired
    private TestimonioService testimonioService;

    // ── Página principal (tienda) ──
    @GetMapping("/")
    public String inicio(@AuthenticationPrincipal UserDetails usuarioLogueado, Model model,
                          HttpServletRequest request) {
        agregarDatosInicio(model, request);

        // Nombre real del usuario logueado (para el saludo "Hola, ___" del navbar)
        if (usuarioLogueado != null) {
            usuarioRepository.findByEmail(usuarioLogueado.getUsername())
                    .ifPresent(u -> model.addAttribute("usuarioNombre", u.getNombre()));
        }

        return "index";
    }

    @GetMapping("/producto/{slug}")
    public String productoPorSlug(@PathVariable String slug,
                                   @AuthenticationPrincipal UserDetails usuarioLogueado, Model model,
                                   HttpServletRequest request) {
        Producto p = productoService.listarActivos().stream()
                .filter(x -> SlugUtil.toSlug(x.getNombre()).equals(slug))
                .findFirst().orElse(null);
        if (p == null) return "redirect:/";

        agregarDatosInicio(model, request);
        if (usuarioLogueado != null) {
            usuarioRepository.findByEmail(usuarioLogueado.getUsername())
                    .ifPresent(u -> model.addAttribute("usuarioNombre", u.getNombre()));
        }

        model.addAttribute("pageTitle", p.getNombre() + " — Full Toldos");
        model.addAttribute("metaDescription", descripcionCorta(p.getDescripcion(), p.getNombre()));
        if (p.getImagenPrincipal() != null) {
            model.addAttribute("ogImage", UrlUtil.baseUrl(request) + p.getImagenPrincipal());
        }

        // Para el schema.org/Product y BreadcrumbList (JSON-LD) del <head>.
        model.addAttribute("producto", p);
        model.addAttribute("precioFinal", p.getPrecioConDescuento() != null ? p.getPrecioConDescuento() : p.getPrecio());
        if (p.getCategoria() != null) {
            model.addAttribute("categoriaNombre", p.getCategoria().getNombre());
            model.addAttribute("categoriaSlug", SlugUtil.toSlug(p.getCategoria().getNombre()));
        }
        return "producto";
    }

    @GetMapping("/categoria/{slug}")
    public String categoriaPorSlug(@PathVariable String slug,
                                    @AuthenticationPrincipal UserDetails usuarioLogueado, Model model,
                                    HttpServletRequest request) {
        Categoria c = "all".equals(slug) ? null : categoriaService.listarActivas().stream()
                .filter(x -> SlugUtil.toSlug(x.getNombre()).equals(slug))
                .findFirst().orElse(null);
        if (c == null && !"all".equals(slug)) return "redirect:/";

        agregarDatosInicio(model, request);
        if (usuarioLogueado != null) {
            usuarioRepository.findByEmail(usuarioLogueado.getUsername())
                    .ifPresent(u -> model.addAttribute("usuarioNombre", u.getNombre()));
        }

        if (c != null) {
            model.addAttribute("pageTitle", c.getNombre() + " — Full Toldos");
            model.addAttribute("metaDescription", descripcionCorta(c.getDescripcion(),
                    "Compra " + c.getNombre() + " en Full Toldos. Fabricación a medida, envíos a todo el Perú."));
            model.addAttribute("categoriaNombre", c.getNombre());
            model.addAttribute("categoriaSlug", slug);
        } else {
            model.addAttribute("pageTitle", "Todos los productos — Full Toldos");
            model.addAttribute("metaDescription", "Explora todo el catálogo de toldos, carpas e inflables publicitarios de Full Toldos.");
        }
        return "categoria";
    }

    @GetMapping("/ofertas")
    public String ofertas(@AuthenticationPrincipal UserDetails usuarioLogueado, Model model,
                           HttpServletRequest request) {
        agregarDatosInicio(model, request);
        if (usuarioLogueado != null) {
            usuarioRepository.findByEmail(usuarioLogueado.getUsername())
                    .ifPresent(u -> model.addAttribute("usuarioNombre", u.getNombre()));
        }
        model.addAttribute("pageTitle", "Ofertas — Full Toldos");
        model.addAttribute("metaDescription",
                "Aprovecha los descuentos especiales en toldos, carpas e inflables publicitarios de Full Toldos.");
        return "ofertas";
    }

    @GetMapping("/cotizar")
    public String cotizar(@AuthenticationPrincipal UserDetails usuarioLogueado, Model model,
                           HttpServletRequest request) {
        agregarDatosInicio(model, request);
        if (usuarioLogueado != null) {
            usuarioRepository.findByEmail(usuarioLogueado.getUsername())
                    .ifPresent(u -> model.addAttribute("usuarioNombre", u.getNombre()));
        }
        model.addAttribute("pageTitle", "Cotiza tu proyecto a medida — Full Toldos");
        model.addAttribute("metaDescription",
                "Solicita una cotización sin compromiso para tu toldo, carpa o estructura a medida. Cuéntanos tus medidas, materiales y necesidades.");
        return "cotizar";
    }

    /** Open Graph / Twitter Card por defecto (para que se vea bien al compartir en WhatsApp/Facebook).
     *  Las páginas que tienen imagen propia (ej. producto) sobreescriben "ogImage" después de llamar a este método. */
    private void agregarOg(Model model, HttpServletRequest request) {
        String baseUrl = UrlUtil.baseUrl(request);
        model.addAttribute("siteUrl", baseUrl);
        model.addAttribute("ogUrl", baseUrl + request.getRequestURI());
        String logoUrl = baseUrl + configService.get("sitio.logo", "/logofulltoldos.png");
        model.addAttribute("orgLogo", logoUrl);
        model.addAttribute("ogImage", logoUrl);
    }

    /** Filtra los links de redes sociales que sí son URLs reales (no el "#" de relleno) para usarlos en JSON-LD (sameAs). */
    private List<String> redesValidas(Map<String, String> redes) {
        return redes.values().stream()
                .filter(v -> v != null && v.startsWith("http"))
                .collect(Collectors.toList());
    }

    /** Descripción corta para meta-tag SEO (~155 caracteres); usa el fallback si no hay descripción cargada. */
    private static String descripcionCorta(String descripcion, String fallback) {
        String base = (descripcion != null && !descripcion.isBlank()) ? descripcion.trim() : fallback;
        return base.length() > 155 ? base.substring(0, 152) + "..." : base;
    }

    /** Datos comunes de la home (contacto, topbar, redes, newsletter, banners, nav, slides, promos) — compartidos por "/", "/producto/{slug}" y "/categoria/{slug}". */
    private void agregarDatosInicio(Model model, HttpServletRequest request) {
        // Datos de contacto desde la DB (editables desde /admin/ajustes)
        String wspNum = configService.get("cotizacion.whatsapp", "51978391237");
        model.addAttribute("wspLink", "https://wa.me/" + wspNum);

        agregarOg(model, request);

        Map<String, String> topbar = new HashMap<>();
        topbar.put("telefono", configService.get("topbar.telefono", "+51 999 999 999"));
        topbar.put("horario",  configService.get("topbar.horario",  "Lun–Sáb 8am–7pm"));
        topbar.put("envios",   configService.get("topbar.envios",   "Envíos a todo el Perú"));
        topbar.put("colorFondo",  configService.get("topbar.color.fondo",  "#b9f6ca"));
        topbar.put("fuente",      configService.get("topbar.fuente",       "'Poppins', sans-serif"));
        topbar.put("colorFuente", configService.get("topbar.color.fuente", "#111111"));
        model.addAttribute("topbar", topbar);

        model.addAttribute("logo",         configService.get("sitio.logo",          "/logofulltoldos.png"));
        model.addAttribute("logoPosicion", configService.get("sitio.logo.posicion", "left"));

        Map<String, String> redes = new HashMap<>();
        redes.put("whatsapp",  configService.get("redes.whatsapp",  "https://wa.me/" + wspNum));
        redes.put("facebook",  configService.get("redes.facebook",  "#"));
        redes.put("instagram", configService.get("redes.instagram", "#"));
        redes.put("tiktok",    configService.get("redes.tiktok",    "#"));
        redes.put("youtube",   configService.get("redes.youtube",   "#"));
        model.addAttribute("redes", redes);
        model.addAttribute("redesSameAs", redesValidas(redes));

        Map<String, String> newsletter = new HashMap<>();
        newsletter.put("titulo",       configService.get("newsletter.titulo",      "Suscríbete a Nuestro Newsletter"));
        newsletter.put("descripcion",  configService.get("newsletter.descripcion", "Novedades, ofertas exclusivas y consejos sobre toldos y carpas."));
        newsletter.put("placeholder",  configService.get("newsletter.placeholder", "Ingresa tu correo electrónico"));
        newsletter.put("boton",        configService.get("newsletter.boton",       "SUSCRIBIRME"));
        newsletter.put("nota",         configService.get("newsletter.nota",        "No te pierdas lo mejor en toldos y carpas."));
        newsletter.put("colorFondo",   configService.get("newsletter.color.fondo", "#2a44d6"));
        newsletter.put("imagen",       configService.get("newsletter.imagen",      "/img/imgfooter.png"));
        model.addAttribute("newsletter", newsletter);

        Map<String, String> bannerCentral = new HashMap<>();
        bannerCentral.put("imagen",    configService.get("banner.central.imagen",    "/img/fulltoldos.png"));
        bannerCentral.put("linkTipo",  configService.get("banner.central.link.tipo", "whatsapp"));
        bannerCentral.put("linkValor", configService.get("banner.central.link.valor", wspNum));
        model.addAttribute("bannerCentral", bannerCentral);

        Map<String, String> buildBanner = new HashMap<>();
        buildBanner.put("imagen",      configService.get("build.banner.imagen",     "/img/construimos.png"));
        buildBanner.put("titulo",      configService.get("build.banner.titulo",      "FABRICADO<br>MEJOR.<br>FABRICADO DE<br>FORMA DIFERENTE."));
        buildBanner.put("descripcion", configService.get("build.banner.descripcion", "Full Toldos, una marca auténtica y original, líder en estructuras de cobertura y toldos a medida en el Perú. Llevamos más de 25 años construyendo sobre nuestro legado de innovación y experiencia en fabricación con productos que marcan la pauta en calidad, durabilidad y facilidad de uso."));
        buildBanner.put("btnTexto",    configService.get("build.banner.btn.texto",   "Más información"));
        buildBanner.put("btnTipo",     configService.get("build.banner.btn.tipo",    "cotizador"));
        buildBanner.put("btnValor",    configService.get("build.banner.btn.valor",   ""));
        buildBanner.put("chatTitulo",  configService.get("build.banner.chat.titulo", "Chatea con un experto."));
        buildBanner.put("chatSub",     configService.get("build.banner.chat.sub",    "Asesoría personalizada por WhatsApp sin costo."));
        model.addAttribute("buildBanner", buildBanner);

        agregarDatosNav(model);

        // Slides del carrusel
        List<?> slides = slideService.listarActivos();
        if (!slides.isEmpty()) model.addAttribute("slides", slides);

        // Bloques promo-banner
        var promoFilas = promoBloqueService.listarActivosPorFila();
        if (!promoFilas.isEmpty()) model.addAttribute("promoFilas", promoFilas);
    }

    /** Datos de categorías/productos para el mega-menú y el buscador del navbar (compartido entre páginas). */
    private void agregarDatosNav(Model model) {
        List<Producto> prods = productoService.listarActivos();

        // Contar productos por categoría
        Map<Long, Long> countByCat = prods.stream()
            .collect(Collectors.groupingBy(p -> p.getCategoria().getId(), Collectors.counting()));

        // Categorías activas → formato JS
        List<Map<String, Object>> dbCats = categoriaService.listarActivas().stream()
            .map(cat -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", String.valueOf(cat.getId()));
                m.put("name", cat.getNombre());
                m.put("img", cat.getImagenUrl() != null ? cat.getImagenUrl() : "");
                m.put("color", "linear-gradient(135deg,#0e3460,#1565c0)");
                m.put("icon", "bi-tags");
                m.put("count", countByCat.getOrDefault(cat.getId(), 0L).intValue());
                return m;
            }).collect(Collectors.toList());

        // Productos activos → formato JS
        List<Map<String, Object>> dbProducts = prods.stream()
            .map(productoCatalogoMapper::toJson)
            .collect(Collectors.toList());

        model.addAttribute("dbCats", dbCats);
        model.addAttribute("dbProducts", dbProducts);
    }

    @GetMapping("/politica-de-privacidad")
    public String politicaPrivacidad(Model model, HttpServletRequest request) {
        addCommonAttrs(model, request);
        return "politica-privacidad";
    }

    @GetMapping("/terminos-y-condiciones")
    public String terminosYCondiciones(Model model, HttpServletRequest request) {
        addCommonAttrs(model, request);
        return "terminos-y-condiciones";
    }

    @GetMapping("/cambios-y-devoluciones")
    public String cambiosYDevoluciones(Model model, HttpServletRequest request) {
        addCommonAttrs(model, request);
        return "cambios-y-devoluciones";
    }

    @GetMapping("/envios")
    public String envios(@AuthenticationPrincipal UserDetails usuarioLogueado, Model model, HttpServletRequest request) {
        addCommonAttrs(model, request);
        agregarDatosNav(model);

        if (usuarioLogueado != null) {
            usuarioRepository.findByEmail(usuarioLogueado.getUsername())
                    .ifPresent(u -> model.addAttribute("usuarioNombre", u.getNombre()));
        }

        model.addAttribute("historial", envioHistorialService.listarActivos());
        model.addAttribute("agencias", envioAgenciaService.listarActivos());
        model.addAttribute("testimonios", testimonioService.listarActivos());

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

        return "envios";
    }

    @GetMapping("/contacto")
    public String contacto(@AuthenticationPrincipal UserDetails usuarioLogueado, Model model, HttpServletRequest request) {
        addCommonAttrs(model, request);
        agregarDatosNav(model);

        if (usuarioLogueado != null) {
            usuarioRepository.findByEmail(usuarioLogueado.getUsername())
                    .ifPresent(u -> model.addAttribute("usuarioNombre", u.getNombre()));
        }

        Map<String, String> contactoForm = new HashMap<>();
        contactoForm.put("tag",         configService.get("contacto.form.tag",         "Contáctenos"));
        contactoForm.put("titulo",      configService.get("contacto.form.titulo",      "¡Estamos aquí para ayudarte!"));
        contactoForm.put("descripcion", configService.get("contacto.form.descripcion", "Si tienes preguntas sobre tu pedido, necesitas ayuda con nuestros productos o simplemente deseas comunicarte con nosotros, no dudes en escribirnos."));
        model.addAttribute("contactoForm", contactoForm);

        return "contacto";
    }

    @GetMapping("/sobre-nosotros")
    public String sobreNosotros(@AuthenticationPrincipal UserDetails usuarioLogueado, Model model, HttpServletRequest request) {
        addCommonAttrs(model, request);
        agregarDatosNav(model);

        if (usuarioLogueado != null) {
            usuarioRepository.findByEmail(usuarioLogueado.getUsername())
                    .ifPresent(u -> model.addAttribute("usuarioNombre", u.getNombre()));
        }

        model.addAttribute("bloques", nosotrosBloqueService.listarActivos());
        model.addAttribute("faqs", preguntaFrecuenteService.listarActivos());

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

        return "sobre-nosotros";
    }

    private void addCommonAttrs(Model model, HttpServletRequest request) {
        agregarOg(model, request);

        String wspNum = configService.get("cotizacion.whatsapp", "51978391237");
        model.addAttribute("wspLink", "https://wa.me/" + wspNum);

        Map<String, String> topbar = new HashMap<>();
        topbar.put("telefono", configService.get("topbar.telefono", "+51 999 999 999"));
        topbar.put("horario",  configService.get("topbar.horario",  "Lun–Sáb 8am–7pm"));
        topbar.put("envios",   configService.get("topbar.envios",   "Envíos a todo el Perú"));
        topbar.put("colorFondo",  configService.get("topbar.color.fondo",  "#b9f6ca"));
        topbar.put("fuente",      configService.get("topbar.fuente",       "'Poppins', sans-serif"));
        topbar.put("colorFuente", configService.get("topbar.color.fuente", "#111111"));
        model.addAttribute("topbar", topbar);

        model.addAttribute("logo",         configService.get("sitio.logo",          "/logofulltoldos.png"));
        model.addAttribute("logoPosicion", configService.get("sitio.logo.posicion", "left"));

        Map<String, String> redes = new HashMap<>();
        redes.put("whatsapp",  configService.get("redes.whatsapp",  "https://wa.me/" + wspNum));
        redes.put("facebook",  configService.get("redes.facebook",  "#"));
        redes.put("instagram", configService.get("redes.instagram", "#"));
        redes.put("tiktok",    configService.get("redes.tiktok",    "#"));
        redes.put("youtube",   configService.get("redes.youtube",   "#"));
        model.addAttribute("redes", redes);
        model.addAttribute("redesSameAs", redesValidas(redes));

        model.addAttribute("ruc", configService.get("empresa.ruc", "RUC: 20XXXXXXXXX"));

        Map<String, String> bannerCentral = new HashMap<>();
        bannerCentral.put("imagen",    configService.get("banner.central.imagen",    "/img/fulltoldos.png"));
        bannerCentral.put("linkTipo",  configService.get("banner.central.link.tipo", "whatsapp"));
        bannerCentral.put("linkValor", configService.get("banner.central.link.valor", wspNum));
        model.addAttribute("bannerCentral", bannerCentral);
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registroForm() {
        return "registro";
    }
    @PostMapping("/registro")
    public String registroProcesar(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmar,
            Model model) {

        if (!password.equals(confirmar)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "registro";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            return "registro";
        }

        try {
            usuarioService.registrarCliente(nombre, email, password);
            return "redirect:/login?registro";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }
}