
// ═══════════════════════════ DATA ═══════════════════════════
const CATS = [
  { id:'toldos', name:'Toldos Publicitarios', icon:'bi-house-door', color:'linear-gradient(135deg,#0e3460,#1565c0)', count:4,
    img:'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&q=75' },
  { id:'carpas', name:'Carpas Armables', icon:'bi-triangle', color:'linear-gradient(135deg,#1a237e,#283593)', count:2,
    img:'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?w=400&q=75' },
  { id:'banderas', name:'Banderas Publicitarias', icon:'bi-flag', color:'linear-gradient(135deg,#006064,#00838f)', count:2,
    img:'https://images.unsplash.com/photo-1526958097901-5e6d742d3371?w=400&q=75' },
  { id:'inflables', name:'Inflables Publicitarios', icon:'bi-balloon', color:'linear-gradient(135deg,#1b5e20,#2e7d32)', count:1,
    img:'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=400&q=75' },
  { id:'juegos', name:'Juegos Publicitarios', icon:'bi-controller', color:'linear-gradient(135deg,#4a148c,#6a1b9a)', count:1,
    img:'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400&q=75' },
  { id:'modulos', name:'Módulos Publicitarios', icon:'bi-grid', color:'linear-gradient(135deg,#bf360c,#d84315)', count:1,
    img:'https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=400&q=75' },
  { id:'terminados', name:'Productos Terminados', icon:'bi-box-seam', color:'linear-gradient(135deg,#1565c0,#1976d2)', count:8,
    img:'https://images.unsplash.com/photo-1553484771-371a605b060b?w=400&q=75' },
  { id:'lonas', name:'Lonas & Textiles', icon:'bi-layers', color:'linear-gradient(135deg,#004d40,#00695c)', count:7,
    img:'https://images.unsplash.com/photo-1558769132-cb1aea458c5e?w=400&q=75' },
];

const PRODUCTS = [
  { id:1, rating:4.8, reviews:124, name:'Toldo Plegable', cat:'toldos', catName:'Toldos Publicitarios', icon:'bi-house-door',
    desc:'Toldo plegable de aluminio anodizado con cobertura de lona PVC 300gr. Resistente al viento y lluvia. Estructura liviana y fácil de montar.',
    longDesc:'Nuestros toldos plegables están fabricados con perfiles de aluminio anodizado de alta resistencia. La lona es PVC de 300gr con tratamiento UV anti-desgarro. Ideal para locales comerciales, ferias y eventos. Disponible en todos los colores y con impresión de logo.',
    features:['Estructura aluminio anodizado','Lona PVC 300gr tratamiento UV','Montaje sin herramientas','Impresión full color disponible','Garantía 2 años estructura'],
    medidas:['2x2m','3x2m','4x2m','4x3m','5x3m','6x3m'],
    colores:['#1565c0','#c62828','#2e7d32','#f57f17','#ffffff','#212121'],
    imgs:[
      'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=600&q=80',
      'https://images.unsplash.com/photo-1503095396549-807759245b35?w=600&q=80',
      'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600&q=80',
    ], badge:'new' },
  { id:2, rating:4.6, reviews:89, name:'Toldo Armable', cat:'toldos', catName:'Toldos Publicitarios', icon:'bi-house-up',
    desc:'Estructura armable de acero galvanizado con cobertura personalizada. Ideal para fachadas comerciales y puestos de mercado.',
    longDesc:'Los toldos armables son perfectos para instalación permanente. Estructura de acero galvanizado calibre 16, resistente a la corrosión. Cobertura de lona PVC de 500gr con costura reforzada en todos los bordes.',
    features:['Acero galvanizado calibre 16','Lona PVC 500gr reforzada','Instalación permanente','Costura ultrasónica','Diseño personalizable'],
    medidas:['2x2m','3x2m','3x3m','4x3m','5x4m'],
    colores:['#1565c0','#c62828','#2e7d32','#ffffff','#f57f17'],
    imgs:[
      'https://images.unsplash.com/photo-1503095396549-807759245b35?w=600&q=80',
      'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=600&q=80',
    ], badge:null },
  { id:3, rating:4.5, reviews:67, name:'Toldo de Pared', cat:'toldos', catName:'Toldos Publicitarios', icon:'bi-building',
    desc:'Toldo adosado a pared con brazo articulado de aluminio. Perfecta solución para fachadas y terrazas comerciales.',
    longDesc:'El toldo de pared con brazo articulado es la solución ideal para locales con fachadas estrechas. Sistema de anclaje reforzado. Lona de PVC impermeable con tratamiento antimanchas.',
    features:['Brazo articulado de aluminio','Sistema de anclaje reforzado','Accionamiento manual o motorizado','Lona antimanchas','Variedad de colores'],
    medidas:['1.5x1m','2x1.5m','3x2m','4x2m'],
    colores:['#1565c0','#c62828','#2e7d32','#f57f17','#ffffff'],
    imgs:[
      'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600&q=80',
      'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=600&q=80',
    ], badge:null },
  { id:4, rating:4.9, reviews:201, name:'Toldo Domo', cat:'toldos', catName:'Toldos Publicitarios', icon:'bi-circle',
    desc:'Diseño curvo exclusivo para máxima resistencia al viento y lluvia. Acabado premium, ideal para restaurantes y cafés.',
    longDesc:'El toldo domo es nuestra línea premium. Su diseño curvo permite el desagüe natural del agua y ofrece mayor resistencia al viento. Estructura de aluminio extruido con lona de PVC 600gr de alta densidad.',
    features:['Aluminio extruido premium','Lona PVC 600gr alta densidad','Desagüe natural integrado','Alta resistencia al viento','Acabado profesional'],
    medidas:['2x2m','3x2m','3x3m','4x3m'],
    colores:['#1565c0','#c62828','#2e7d32','#ffffff'],
    imgs:[
      'https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=600&q=80',
      'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600&q=80',
    ], badge:'hot' },
  { id:5, rating:4.7, reviews:156, name:'Carpa a Dos Aguas', cat:'carpas', catName:'Carpas Armables', icon:'bi-triangle',
    desc:'Carpa con techo a dos aguas, estructura robusta de acero galvanizado. Disponible desde 2x2 hasta 5x5 metros.',
    longDesc:'Nuestras carpas a dos aguas son la opción más popular para ferias y eventos al aire libre. La estructura piramidal de acero galvanizado garantiza máxima estabilidad. Cobertura de lona PVC de doble cara reforzada con costuras ultrasónicas.',
    features:['Estructura acero galvanizado','Lona PVC doble cara','Costuras ultrasónicas','Fácil montaje 2 personas','Anclajes incluidos'],
    medidas:['2x2m','2x3m','3x3m','3x4m','3x6m','4x4m','4x6m','5x5m'],
    colores:['#ffffff','#1565c0','#c62828','#2e7d32','#f57f17','#212121'],
    imgs:[
      'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?w=600&q=80',
      'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=600&q=80',
      'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&q=80',
    ], badge:'new' },
  { id:6, rating:4.5, reviews:73, name:'Carpa Techo Plano', cat:'carpas', catName:'Carpas Armables', icon:'bi-stop-fill',
    desc:'Carpa de techo plano con estructura de acero galvanizado. Ideal para ferias, eventos corporativos y mercados.',
    longDesc:'La carpa de techo plano es ideal para espacios urbanos donde la altura es limitada. Su diseño modular permite combinar varias unidades para crear grandes espacios cubiertos.',
    features:['Sistema modular combinable','Estructura acero galvanizado','Lona PVC 450gr','Paredes laterales opcionales','Ventanas de PVC transparente'],
    medidas:['2x2m','3x3m','3x6m','4x4m','4x8m','6x6m'],
    colores:['#ffffff','#1565c0','#c62828','#2e7d32'],
    imgs:[
      'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=600&q=80',
      'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?w=600&q=80',
    ], badge:null },
  { id:7, rating:4.4, reviews:45, name:'Pluma Publicitaria', cat:'banderas', catName:'Banderas Publicitarias', icon:'bi-flag-fill',
    desc:'Bandera tipo pluma de alto impacto visual. Sublimación full color en poliéster de alta resistencia. Incluye mástil y base.',
    longDesc:'Las plumas publicitarias son la herramienta más efectiva para captar la atención. Impresión por sublimación en poliéster 110gr, colores vivos que no se desvanecen. Mástil de fibra de vidrio flexible y resistente al viento.',
    features:['Sublimación full color','Poliéster 110gr anti-UV','Mástil fibra de vidrio','Base cruceta o tornillo','Funda de transporte incluida'],
    medidas:['2.5m','3.5m','4.5m','5.5m'],
    colores:['#ffffff','#1565c0','#c62828','#2e7d32','#f57f17'],
    imgs:[
      'https://images.unsplash.com/photo-1526958097901-5e6d742d3371?w=600&q=80',
      'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=600&q=80',
    ], badge:null },
  { id:8, rating:4.6, reviews:38, name:'Gota Publicitaria', cat:'banderas', catName:'Banderas Publicitarias', icon:'bi-droplet-fill',
    desc:'Bandera con forma de gota, efecto visual llamativo. Sublimación full color. Incluye mástil de aluminio y base.',
    longDesc:'La bandera gota es una forma distintiva que se diferencia en cualquier evento. Diseño aerodinámico que mantiene la bandera siempre visib incluso con poco viento. Impresión ambas caras disponible.',
    features:['Diseño aerodinámico','Impresión 1 o 2 caras','Aluminio anodizado','Sin arrugas garantizado','Varios tamaños'],
    medidas:['1.5m','2m','3m','4m'],
    colores:['#ffffff','#1565c0','#c62828','#2e7d32','#f57f17','#212121'],
    imgs:[
      'https://images.unsplash.com/photo-1526958097901-5e6d742d3371?w=600&q=80',
    ], badge:'sale' },
  { id:9, rating:4.8, reviews:112, name:'Inflable Publicitario', cat:'inflables', catName:'Inflables Publicitarios', icon:'bi-balloon-fill',
    desc:'Inflables personalizados en cualquier forma y tamaño. Tela Oxford de alta resistencia. Ventilador incluido.',
    longDesc:'Nuestros inflables publicitarios son fabricados en tela Oxford 420D de altísima resistencia. Impresión digital full color directamente sobre la tela. Incluye ventilador de alta eficiencia, sistema de sujeción y funda de transporte.',
    features:['Tela Oxford 420D','Impresión digital full color','Ventilador incluido','Cualquier forma y tamaño','Certificado de seguridad'],
    medidas:['1m','2m','3m','5m','Personalizado'],
    colores:['Personalizado'],
    imgs:[
      'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=600&q=80',
      'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&q=80',
    ], badge:'hot' },
  { id:10, rating:4.7, reviews:85, name:'Juego Inflable', cat:'juegos', catName:'Juegos Publicitarios', icon:'bi-controller',
    desc:'Juegos inflables para eventos y campañas publicitarias. Branding full color. Seguros y certificados.',
    longDesc:'Los juegos inflables combinan entretenimiento con publicidad. Perfectos para activaciones de marca, eventos corporativos y espacios para niños. Fabricados bajo normas de seguridad internacionales.',
    features:['Normas de seguridad internacionales','Branding personalizado','Fácil instalación 15min','Incluye ventilador','Reparaciones sin costo 1 año'],
    medidas:['3x3m','4x4m','5x5m','Personalizado'],
    colores:['Personalizado'],
    imgs:[
      'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&q=80',
    ], badge:null },
  { id:11, rating:4.5, reviews:54, name:'Módulo Publicitario', cat:'modulos', catName:'Módulos Publicitarios', icon:'bi-grid-3x3',
    desc:'Módulos armables para ferias, stands y exposiciones. Sistema modular, fácil transporte y montaje rápido.',
    longDesc:'Los módulos publicitarios son la solución profesional para ferias y exposiciones. Sistema de conexión rápida sin tornillos. Paneles intercambiables con impresión en alta resolución.',
    features:['Sistema snap sin tornillos','Paneles impresión HD','Transporte en maletín','Iluminación LED opcional','Configuración flexible'],
    medidas:['2x1m','3x1m','4x2m','6x2m','Personalizado'],
    colores:['Personalizado'],
    imgs:[
      'https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=600&q=80',
      'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=600&q=80',
    ], badge:null },
  { id:12, rating:4.6, reviews:178, name:'Cobertura de Toldo', cat:'terminados', catName:'Productos Terminados', icon:'bi-shield-check',
    desc:'Coberturas de repuesto para toldos de todas las medidas. Lona PVC de primera calidad. Fabricación exacta a medida.',
    longDesc:'¿Tu toldo necesita una cobertura nueva? Fabricamos coberturas de repuesto con medidas exactas de tu estructura existente. Lona PVC en todos los gramajes y colores.',
    features:['Fabricación a medida exacta','PVC 300gr, 450gr o 600gr','Todos los colores disponibles','Dobladillo reforzado','Entrega en 5-7 días hábiles'],
    medidas:['A medida'],
    colores:['#ffffff','#1565c0','#c62828','#2e7d32','#f57f17','#212121'],
    imgs:[
      'https://images.unsplash.com/photo-1553484771-371a605b060b?w=600&q=80',
    ], badge:null },
  { id:13, rating:4.7, reviews:92, name:'Estructura para Carpa', cat:'terminados', catName:'Productos Terminados', icon:'bi-tools',
    desc:'Estructuras completas para carpas de acero galvanizado. Piramidal y a dos aguas. Desde 2x2 hasta 5x5 metros.',
    longDesc:'Vendemos estructuras sin cobertura para quienes necesitan solo el armazón. Acero galvanizado electrolítico de máxima resistencia a la corrosión. Incluye todos los accesorios de montaje.',
    features:['Acero galvanizado electrolítico','Todos los accesorios incluidos','Pintura hornoreada','Manual de montaje','Garantía 5 años estructura'],
    medidas:['2x2m','3x3m','4x4m','5x5m'],
    colores:['Galvanizado natural','Blanco','Negro'],
    imgs:[
      'https://images.unsplash.com/photo-1503095396549-807759245b35?w=600&q=80',
    ], badge:null },
  { id:14, rating:4.3, reviews:211, name:'Malla PVC', cat:'lonas', catName:'Lonas & Textiles', icon:'bi-grid-1x2',
    desc:'Malla de PVC para ventilación y protección solar. 50%, 70% y 90% de sombra. Venta por metro cuadrado.',
    longDesc:'Las mallas PVC son ideales para cerramientos que requieren ventilación. Disponible en diferentes porcentajes de sombra para controlar la temperatura y la luz solar.',
    features:['50%, 70% o 90% de sombra','Resistente a UV','Lavable y duradera','Refuerzo en orillas','Venta por metro o rollo'],
    medidas:['Por metro','Rollo 50m','Rollo 100m'],
    colores:['#2e7d32','#1565c0','#212121','#9e9e9e'],
    imgs:[
      'https://images.unsplash.com/photo-1558769132-cb1aea458c5e?w=600&q=80',
    ], badge:'new' },
  { id:15, rating:4.8, reviews:143, name:'Lona PVC Taiwan', cat:'lonas', catName:'Lonas & Textiles', icon:'bi-layers',
    desc:'Lona PVC ambas caras de resistencia premium. Ideal para coberturas de alto tráfico y uso intensivo.',
    longDesc:'La lona Taiwan es nuestra lona de mayor calidad. Fabricada con PVC de doble cara con trama interna de poliéster de alta densidad. Resistencia al rasgado superior a 800N.',
    features:['PVC doble cara','Trama poliéster alta densidad','Resistencia >800N al rasgado','Anti-hongos y bacterias','Certificación ISO'],
    medidas:['Por metro','Rollo 50m'],
    colores:['#ffffff','#1565c0','#c62828','#2e7d32','#f57f17','#212121'],
    imgs:[
      'https://images.unsplash.com/photo-1558769132-cb1aea458c5e?w=600&q=80',
      'https://images.unsplash.com/photo-1553484771-371a605b060b?w=600&q=80',
    ], badge:null },
];

// ── Precios por producto (desde la medida base) ──
const PRICES = {
  1: {precio:289, precioAntes:399},
  2: {precio:350, precioAntes:480},
  3: {precio:195, precioAntes:260},
  4: {precio:420, precioAntes:580},
  5: {precio:650, precioAntes:890},
  6: {precio:580, precioAntes:780},
  7: {precio:175, precioAntes:240},
  8: {precio:210, precioAntes:290},
  9: {precio:890, precioAntes:1200},
  10:{precio:1200,precioAntes:1600},
  11:{precio:750, precioAntes:990},
  12:{precio:148, precioAntes:200},
  13:{precio:480, precioAntes:650},
  14:{precio:18,  precioAntes:25},
  15:{precio:35,  precioAntes:48},
};

function getStarsHTML(p) {
  if(!p.rating) return '';
  const full = Math.floor(p.rating);
  const half = (p.rating - full) >= 0.5 ? 1 : 0;
  const empty = 5 - full - half;
  let s = '<span class="prod-stars">';
  for(let i=0;i<full;i++)  s += '<i class="bi bi-star-fill"></i>';
  if(half)                 s += '<i class="bi bi-star-half"></i>';
  for(let i=0;i<empty;i++) s += '<i class="bi bi-star"></i>';
  s += `<span class="prod-reviews">${p.rating.toFixed(1)} (${p.reviews})</span></span>`;
  return s;
}

function getPriceHTML(p, large) {
  const pr = PRICES[p.id];
  if(!pr) return `<span class="prod-price" style="color:var(--azul-claro);">Consultar</span>`;
  const sz = large ? 'font-size:2.2rem' : 'font-size:1.5rem';
  return `<div class="prod-price-block">
    <span class="prod-price-now" style="${sz}">S/ ${pr.precio.toLocaleString('es-PE')}</span>
  </div>`;
}
function getWspCardBtn(p) {
  const msg = encodeURIComponent(`Hola Full Toldos! Me interesa: *${p.name}*. Quisiera una cotización.`);
  return `<a class="btn-wsp-card" href="https://wa.me/51999999999?text=${msg}" target="_blank">
    <i class="bi bi-whatsapp"></i> Pedir
  </a>`;
}

const MATS = [
  { icon:'bi-droplet', name:'Polietileno Alta Densidad', sub:'Tarflex — Lona Impermeable',
    desc:'Material impermeable de alta resistencia UV. Disponible en 200gr y 300gr, anchos de 2 y 4 metros.',
    tags:['TARFLEX 200GR','TARFLEX 300GR','2MTS','4MTS','RESISTENTE UV'] },
  { icon:'bi-layers', name:'Lona PVC Técnica', sub:'Una cara · Ambas caras',
    desc:'Lona PVC plastificada de alta durabilidad. Versión box, pesada y Taiwan. Resistencia superior a desgarros.',
    tags:['LINO BOX','LINO PESADO','LONA TAIWAN','ANTI-UV','IMPERMEABLE'] },
  { icon:'bi-eye', name:'PVC Transparente & Mallas', sub:'Mica — Sombra — Ventilación',
    desc:'PVC transparente para ventanas de carpas. Mallas en distintos porcentajes de sombra.',
    tags:['PVC MICA','MALLAS 50%','MALLAS 70%','MALLAS 90%','ANTI-RAYOS UV'] },
  { icon:'bi-gear', name:'Acero Galvanizado', sub:'Estructuras — Embones — Repuestos',
    desc:'Estructuras de acero galvanizado y zincado para toldos y carpas. Perfiles piramidales de 2x2 hasta 5x5m.',
    tags:['GALVANIZADO','ZINCADO','LAC','ESTRUCTURAS','EMBONES','REPUESTOS'] },
  { icon:'bi-scissors', name:'Insumos de Costura', sub:'Pega Pega — Cierres — Ojalillos',
    desc:'Todo lo necesario para fabricar y reparar toldos. Pega pega, cinta rígida, cierres tractor, hilo industrial.',
    tags:['PEGA PEGA','CINTA RÍGIDA','CIERRE TRACTOR','OJALILLOS','HILO'] },
  { icon:'bi-wrench', name:'Accesorios', sub:'Templadores — Candados — Cable',
    desc:'Accesorios de fijación y tensado para estructuras de toldos y carpas. Templadores, candados y cable acerado.',
    tags:['TEMPLADORES','CANDADOS','CABLE ACERADO','SIN FIN','TROQUELADORA'] },
];

const WHY = [
  { icon:'bi-award', title:'25+ Años de Experiencia', desc:'Más de dos décadas fabricando coberturas de alta calidad. Conocemos cada detalle del oficio.' },
  { icon:'bi-rulers', title:'Fabricación a Medida', desc:'Cada producto se fabrica según tus especificaciones exactas. Sin medidas estándar que no se adaptan a tu espacio.' },
  { icon:'bi-shield-check', title:'Materiales Certificados', desc:'Solo trabajamos con proveedores certificados. Lonas PVC, acero galvanizado y textiles técnicos de primera.' },
  { icon:'bi-truck', title:'Entrega Puntual', desc:'Cumplimos los plazos acordados. Coordinamos entrega e instalación en todo Lima y provincias.' },
  { icon:'bi-headset', title:'Soporte Post-Venta', desc:'Te acompañamos después de la venta con repuestos, mantenimiento y asesoría técnica.' },
  { icon:'bi-currency-dollar', title:'Precios Competitivos', desc:'Fábrica directa = precios sin intermediarios. Cotizaciones transparentes en menos de 24 horas.' },
];

// ═══════════════════════════ STATE ═══════════════════════════
let cart = [];
let activeFilter = 'all';
let visibleCount = 9;
let currentSlide = 0;
let slideInterval;

// ═══════════════════════════ TICKER ═══════════════════════════
function buildTicker() {
  const items = CATS.map(c => c.name).concat(['Fabricación a Medida','Acero Galvanizado','Lonas PVC','Lima y Provincias']);
  const doubled = [...items,...items];
  document.getElementById('tickerTrack').innerHTML = doubled.map(i =>
    `<span class="ticker-item"><span class="ticker-dot"></span>${i}</span>`).join('');
}

// ═══════════════════════════ HERO SLIDER ═══════════════════════════
function goSlide(idx) {
  document.querySelectorAll('.hero-slide').forEach((s,i) => {
    s.classList.toggle('active', i === idx);
  });
  document.querySelectorAll('.hero-dot').forEach((d,i) => {
    d.classList.toggle('active', i === idx);
  });
  currentSlide = idx;
}
function nextSlide() { goSlide((currentSlide+1) % 3); }
function prevSlide() { goSlide((currentSlide+2) % 3); }
function startAutoSlide() {
  slideInterval = setInterval(nextSlide, 5000);
}
document.querySelectorAll('.hero-prev,.hero-next,.hero-dot').forEach(el => {
  el.addEventListener('click', () => { clearInterval(slideInterval); startAutoSlide(); });
});

// ═══════════════════════════ CATEGORIES ═══════════════════════════
function buildCats() {
  document.getElementById('catGrid').innerHTML = CATS.map(c => `
    <div class="cat-card" onclick="openCatPage('${c.id}')">
      <div class="cat-arrow"><i class="bi bi-arrow-right"></i></div>
      <div class="cat-img-area" style="background:${c.color}">
        <img src="${c.img}" alt="${c.name}" class="cat-img-real" loading="lazy"
          onerror="this.style.display='none';this.nextElementSibling.style.display='flex'">
        <div class="cat-img-icon" style="display:none;position:absolute;font-size:3.5rem;color:rgba(255,255,255,0.5)"><i class="bi ${c.icon}"></i></div>
        <div class="cat-overlay"></div>
      </div>
      <div class="cat-body">
        <div class="cat-name">${c.name}</div>
        <div class="cat-count">${c.count} producto${c.count>1?'s':''}</div>
      </div>
    </div>
  `).join('');
}

// ═══════════════════════════ FILTER TABS ═══════════════════════════
function buildFilterTabs() { /* tabs eliminados */ }
function filterProducts(catId) {
  activeFilter = catId; visibleCount = PRODUCTS.length; renderProducts();
}

// ═══════════════════════════ PRODUCTS ═══════════════════════════
function getGradient(cat) {
  const map = {
    toldos:'linear-gradient(135deg,#e3f2fd,#bbdefb)',
    carpas:'linear-gradient(135deg,#e8eaf6,#c5cae9)',
    banderas:'linear-gradient(135deg,#e0f2f1,#b2dfdb)',
    inflables:'linear-gradient(135deg,#e8f5e9,#c8e6c9)',
    juegos:'linear-gradient(135deg,#f3e5f5,#e1bee7)',
    modulos:'linear-gradient(135deg,#fbe9e7,#ffccbc)',
    terminados:'linear-gradient(135deg,#e3f2fd,#b3e5fc)',
    lonas:'linear-gradient(135deg,#e0f2f1,#b2dfdb)',
  };
  return map[cat] || 'linear-gradient(135deg,#e3f2fd,#bbdefb)';
}
function renderProducts() {
  const grid = document.getElementById('productsGrid');
  if(!grid) return;
  grid.innerHTML = PRODUCTS.map(p => `
    <div class="prod-card" onclick="openModal(${p.id})">
      <div class="prod-img-area" style="background:${getGradient(p.cat)}">
        ${p.imgs && p.imgs[0] ? `<img src="${p.imgs[0]}" alt="${p.name}" class="prod-img-real" loading="lazy"
          onerror="this.style.display='none';this.parentElement.querySelector('.prod-img-placeholder').style.display='flex'">
          <div class="prod-img-placeholder" style="display:none;position:absolute;inset:0"><i class="bi ${p.icon}" style="font-size:3rem;color:rgba(0,0,0,0.2)"></i></div>` :
          `<div class="prod-img-placeholder"><i class="bi ${p.icon}" style="font-size:3rem;color:rgba(0,0,0,0.2)"></i></div>`}
        <div class="prod-badge-wrap">
          ${p.badge==='new'?'<span class="prod-badge badge-new">Nuevo</span>':''}
          ${p.badge==='hot'?'<span class="prod-badge badge-hot">Popular</span>':''}
          ${p.badge==='sale'?'<span class="prod-badge badge-sale">Oferta</span>':''}
        </div>
        <button class="prod-eye-btn" onclick="event.stopPropagation();openProductPage(${p.id})" title="Ver producto completo"><i class="bi bi-eye"></i></button>
      </div>
      <div class="prod-body">
        <div class="prod-cat-tag">${p.catName}</div>
        <div class="prod-name">${p.name}</div>
        ${getStarsHTML(p)}
        <div class="prod-desc">${p.desc.substring(0,95)}...</div>
        <div class="prod-footer">
          <div>${getPriceHTML(p)}<div class="prod-price-note">desde med. base</div></div>
          ${getWspCardBtn(p)}
        </div>
      </div>
    </div>
  `).join('');
}
function loadMoreProds() { renderProducts(); }

// ═══════════════════════════ MODAL ═══════════════════════════
let currentModalProduct = null;
let selectedMedida = '';
let selectedColor = '';
let currentImgIdx = 0;

// openModal now routes to the full product page
function openModal(id) { openProductPage(id); }
function closeModal() { closeProdPage(); }
function closeModalOutside(e) { if(e.target===document.getElementById('modalOverlay')) closeProdPage(); }

// ═══════════════════════════ PRODUCT PAGE ═══════════════════════════
let prevPageView = 'main';

function openProductPage(id) {
  const p = PRODUCTS.find(x => x.id === id);
  if(!p) return;
  prevPageView = document.getElementById('catPageView').style.display !== 'none' ? 'category' : 'main';
  currentModalProduct = p;
  selectedMedida = p.medidas[0] || '';
  selectedColor = p.colores[0] || '';
  currentImgIdx = 0;
  document.getElementById('mainView').style.display = 'none';
  document.getElementById('catPageView').style.display = 'none';
  document.getElementById('prodPageView').style.display = 'block';
  renderProdPage(p);
  setTimeout(initProductZoom, 80);
  renderTambienInteresa('prodTambienSection', p.cat, [p.id]);
  window.scrollTo({top:0, behavior:'smooth'});
}

function closeProdPage() {
  cleanupProductZoom();
  document.getElementById('prodPageView').style.display = 'none';
  if(prevPageView === 'category') {
    document.getElementById('catPageView').style.display = 'block';
  } else {
    document.getElementById('mainView').style.display = 'block';
  }
  window.scrollTo({top:0, behavior:'smooth'});
}

function renderProdPage(p) {
  // Breadcrumb
  document.getElementById('prodBreadcrumb').innerHTML =
    `<a href="#" onclick="closeProdPage();return false;">Inicio</a> / ` +
    `<a href="#" onclick="openCatPage('${p.cat}');return false;">${p.catName}</a> / ` +
    `<strong>${p.name.toUpperCase()}</strong>`;

  // Nav arrows
  const sameCat = PRODUCTS.filter(x => x.cat === p.cat);
  const idx = sameCat.findIndex(x => x.id === p.id);
  const prevBtn = document.getElementById('prodNavPrevBtn');
  const nextBtn = document.getElementById('prodNavNextBtn');
  if(prevBtn) prevBtn.disabled = idx <= 0;
  if(nextBtn) nextBtn.disabled = idx >= sameCat.length - 1;

  // Thumbnails + main image
  const thumbStrip = document.getElementById('prodThumbStrip');
  const mainImg = document.getElementById('prodMainImg');
  if(p.imgs && p.imgs.length) {
    thumbStrip.innerHTML = p.imgs.map((img,i) => `
      <img src="${img}" alt="${p.name} ${i+1}" class="pthumb ${i===0?'active':''}"
        onclick="changeProdImg(${i})" loading="lazy"
        onerror="this.style.display='none'">`).join('');
    mainImg.src = p.imgs[0];
    mainImg.alt = p.name;
  } else {
    thumbStrip.innerHTML = '';
    mainImg.src = `https://via.placeholder.com/600x460/e3f2fd/1565c0?text=${encodeURIComponent(p.name)}`;
  }

  // Flechas de imagen
  const imgArea = document.querySelector('#prodPageView .prod-main-img-area');
  if(imgArea) {
    imgArea.querySelectorAll('.prod-img-arrow').forEach(a => a.remove());
    if(p.imgs && p.imgs.length > 1) {
      const btnP = document.createElement('button');
      btnP.className = 'prod-img-arrow prod-img-arrow-prev';
      btnP.id = 'prodImgPrev';
      btnP.innerHTML = '<i class="bi bi-chevron-left"></i>';
      btnP.disabled = true;
      btnP.onclick = () => { if(currentImgIdx > 0) changeProdImg(currentImgIdx - 1); };

      const btnN = document.createElement('button');
      btnN.className = 'prod-img-arrow prod-img-arrow-next';
      btnN.id = 'prodImgNext';
      btnN.innerHTML = '<i class="bi bi-chevron-right"></i>';
      btnN.disabled = p.imgs.length <= 1;
      btnN.onclick = () => { if(currentImgIdx < p.imgs.length - 1) changeProdImg(currentImgIdx + 1); };

      imgArea.appendChild(btnP);
      imgArea.appendChild(btnN);
    }
  }

  // Info card
  const colorBlock = p.colores[0] !== 'Personalizado' ? `
    <span class="prod-page-section-lbl">Colores disponibles</span>
    <div class="color-selector" style="margin-bottom:20px;" id="ppColorSel">
      ${p.colores.map(c=>`<div class="color-opt ${c===selectedColor?'active':''}"
        style="background:${c};border:2px solid ${c==='#ffffff'?'#ccc':'transparent'}"
        onclick="ppSelectColor('${c}')" title="${c}"></div>`).join('')}
    </div>` :
    `<div style="background:#e3f2fd;border-radius:8px;padding:12px 14px;margin-bottom:20px;font-size:0.82rem;color:var(--azul-claro);">
      <i class="bi bi-palette-fill"></i> <strong>Personalizado:</strong> Cualquier color, logo o diseño a tu medida.
    </div>`;

  document.getElementById('prodInfoCard').innerHTML = `
    <div class="prod-page-cat-tag">${p.catName}</div>
    <div class="prod-page-name">${p.name}</div>
    ${getStarsHTML(p)}
    <div class="prod-page-desc">${p.longDesc}</div>
    <span class="prod-page-section-lbl">Características principales</span>
    <ul class="prod-page-features">
      ${p.features.map(f=>`<li><i class="bi bi-check-circle-fill"></i>${f}</li>`).join('')}
    </ul>
    <span class="prod-page-section-lbl">Medidas disponibles</span>
    <div class="medidas-grid" style="margin-bottom:20px;" id="ppMedidasGrid">
      ${p.medidas.map(m=>`<button class="medida-btn ${m===selectedMedida?'active':''}"
        onclick="ppSelectMedida('${m}')">${m}</button>`).join('')}
    </div>
    ${colorBlock}
    ${getPriceHTML(p, true)}
    <div class="prod-page-price-note" style="margin-top:10px;">
      <i class="bi bi-info-circle-fill" style="color:var(--azul-claro)"></i>
      Medida seleccionada: <strong id="ppMedidaSelected" style="color:var(--azul-marino)">${selectedMedida}</strong>
      <span style="color:#aaa;font-size:0.72rem;">· precio desde med. base, sin IGV</span>
    </div>
    <button class="btn-wsp-page" onclick="cotizarWspPage()">
      <i class="bi bi-whatsapp"></i> Pedido por WhatsApp
    </button>
    <button class="btn-cart-page" onclick="addCartById(${p.id});showToast('${p.name} agregado al carrito')">
      <i class="bi bi-bag-plus-fill"></i> Agregar al carrito de consulta
    </button>
    <div class="prod-price-excl">*Precios no incluyen IGV</div>
  `;

  // Related products scroll
  const related = PRODUCTS.filter(x => x.cat === p.cat && x.id !== p.id);
  const relSection = document.getElementById('prodRelatedSection');
  if(relSection) {
    if(related.length) {
      relSection.innerHTML = `
        <div class="prod-related-section">
          <div class="prod-related-header">
            <div class="prod-related-title">Productos Relacionados</div>
            <span class="prod-related-count">${related.length} producto${related.length!==1?'s':''} en ${p.catName}</span>
          </div>
          <div class="prod-related-scroll">
            ${related.map(rp => `
              <div class="prod-card" onclick="openProductPage(${rp.id})">
                <div class="prod-img-area" style="background:${getGradient(rp.cat)}">
                  ${rp.imgs && rp.imgs[0]
                    ? `<img src="${rp.imgs[0]}" alt="${rp.name}" class="prod-img-real" loading="lazy"
                        onerror="this.style.display='none';this.parentElement.querySelector('.prod-img-placeholder').style.display='flex'">
                       <div class="prod-img-placeholder" style="display:none;position:absolute;inset:0"><i class="bi ${rp.icon}" style="font-size:3rem;color:rgba(21,101,192,0.35)"></i></div>`
                    : `<div class="prod-img-placeholder"><i class="bi ${rp.icon}" style="font-size:3rem;color:rgba(21,101,192,0.35)"></i></div>`}
                  <div class="prod-badge-wrap">
                    ${rp.badge==='new'?'<span class="prod-badge badge-new">Nuevo</span>':''}
                    ${rp.badge==='hot'?'<span class="prod-badge badge-hot">Popular</span>':''}
                    ${rp.badge==='sale'?'<span class="prod-badge badge-sale">Oferta</span>':''}
                  </div>
                  <div class="prod-actions-hover">
                    <div class="act-btn" onclick="addCart(event,${rp.id})" title="Agregar al carrito"><i class="bi bi-bag-plus"></i></div>
                  </div>
                </div>
                <div class="prod-body">
                  <div class="prod-cat-tag">${rp.catName}</div>
                  <div class="prod-name">${rp.name}</div>
                  ${getStarsHTML(rp)}
                  <div class="prod-desc">${rp.desc.substring(0,82)}...</div>
                  <div class="prod-footer">
                    <div>
                      ${getPriceHTML(rp)}
                      <div class="prod-price-note">desde</div>
                    </div>
                    <button class="btn-add" onclick="event.stopPropagation();openProductPage(${rp.id})">
                      <i class="bi bi-eye"></i> Ver
                    </button>
                  </div>
                </div>
              </div>
            `).join('')}
          </div>
        </div>
      `;
    } else {
      relSection.innerHTML = '';
    }
  }
}

function changeProdImg(idx) {
  if(!currentModalProduct) return;
  currentImgIdx = idx;
  const imgs = currentModalProduct.imgs || [];
  const img = document.getElementById('prodMainImg');
  if(img && imgs[idx]) {
    img.style.opacity = '0';
    setTimeout(() => {
      img.src = imgs[idx]; img.style.opacity = '1';
      const r = document.getElementById('zoomResult');
      if(r) r.style.backgroundImage = `url('${imgs[idx]}')`;
    }, 150);
  }
  document.querySelectorAll('.pthumb').forEach((t,i) => t.classList.toggle('active', i===idx));
  // Actualizar flechas
  const btnP = document.getElementById('prodImgPrev');
  const btnN = document.getElementById('prodImgNext');
  if(btnP) btnP.disabled = idx <= 0;
  if(btnN) btnN.disabled = idx >= imgs.length - 1;
}

function ppSelectMedida(m) {
  selectedMedida = m;
  document.querySelectorAll('#ppMedidasGrid .medida-btn').forEach(b => b.classList.toggle('active', b.textContent.trim()===m));
  const el = document.getElementById('ppMedidaSelected');
  if(el) el.textContent = m;
}

function ppSelectColor(c) {
  selectedColor = c;
  document.querySelectorAll('#ppColorSel .color-opt').forEach(o => o.classList.toggle('active', o.style.background===c));
}

function cotizarWspPage() {
  const p = currentModalProduct;
  if(!p) return;
  const msg = `Hola Full Toldos! Me interesa cotizar:%0A• *${p.name}*%0A• Medida: ${selectedMedida}%0A• Cantidad: 1 unidad%0APor favor envíame el precio.`;
  window.open(`https://wa.me/51999999999?text=${msg}`,'_blank');
}

function prodNavPrev() {
  if(!currentModalProduct) return;
  const sameCat = PRODUCTS.filter(x => x.cat === currentModalProduct.cat);
  const idx = sameCat.findIndex(x => x.id === currentModalProduct.id);
  if(idx > 0) openProductPage(sameCat[idx-1].id);
}

function prodNavNext() {
  if(!currentModalProduct) return;
  const sameCat = PRODUCTS.filter(x => x.cat === currentModalProduct.cat);
  const idx = sameCat.findIndex(x => x.id === currentModalProduct.id);
  if(idx < sameCat.length - 1) openProductPage(sameCat[idx+1].id);
}

function renderModal(p) {
  const mainImg = (p.imgs && p.imgs[currentImgIdx]) ? p.imgs[currentImgIdx]
    : `https://via.placeholder.com/600x360/e3f2fd/1565c0?text=${encodeURIComponent(p.name)}`;

  document.getElementById('modalLeftCol').innerHTML = `
    <div class="modal-left">
      <img src="${mainImg}" alt="${p.name}" class="modal-main-img" id="modalMainImg">
      <div class="modal-thumbs" id="modalThumbs">
        ${(p.imgs||[]).map((img,i) => `
          <img src="${img}" alt="img${i+1}" class="modal-thumb ${i===currentImgIdx?'active':''}"
            onclick="changeModalImg(${i})" loading="lazy"
            onerror="this.style.display='none'">
        `).join('')}
      </div>
    </div>
  `;

  document.getElementById('modalRightCol').innerHTML = `
    <div class="modal-right">
      <div class="modal-cat-tag">${p.catName}</div>
      <div class="modal-name">${p.name}</div>
      <div class="modal-desc">${p.longDesc}</div>

      <span class="modal-section-lbl">Características principales</span>
      <ul class="modal-features mb-3">
        ${p.features.map(f=>`<li><i class="bi bi-check-circle-fill"></i>${f}</li>`).join('')}
      </ul>

      <span class="modal-section-lbl">Medidas disponibles</span>
      <div class="medidas-grid" id="medidasGrid">
        ${p.medidas.map(m=>`
          <button class="medida-btn ${m===selectedMedida?'active':''}"
            onclick="selectMedida('${m}')">${m}</button>
        `).join('')}
      </div>

      ${p.colores[0] !== 'Personalizado' ? `
        <span class="modal-section-lbl">Colores disponibles</span>
        <div class="color-selector" id="colorSelector">
          ${p.colores.map(c=>`
            <div class="color-opt ${c===selectedColor?'active':''}"
              style="background:${c};border:2px solid ${c==='#ffffff'?'#ccc':'transparent'}"
              onclick="selectColor('${c}')" title="${c}"></div>
          `).join('')}
        </div>
      ` : `
        <div style="background:#e3f2fd;border-radius:8px;padding:12px 14px;margin-bottom:16px;font-size:0.82rem;color:var(--azul-claro);">
          <i class="bi bi-palette-fill"></i> <strong>Personalizado:</strong> Cualquier color, logo o diseño a tu medida.
        </div>
      `}

      <div class="modal-price-row">
        ${getPriceHTML(p, false)}
        <div class="modal-price-note">Sin IGV · desde med. base</div>
      </div>
      <div style="background:#f0f7ff;border-radius:8px;padding:10px 14px;margin-bottom:16px;font-size:0.8rem;color:#546e7a;display:flex;align-items:center;gap:8px;">
        <i class="bi bi-info-circle-fill" style="color:var(--azul-claro)"></i>
        Medida seleccionada: <strong id="medidaSelected" style="color:var(--azul-marino)">${selectedMedida}</strong>
      </div>
      <button class="btn-wsp" onclick="cotizarWsp(${p.id})">
        <i class="bi bi-whatsapp"></i> Cotizar por WhatsApp
      </button>
      <button class="btn-add-cart-modal" onclick="addCartModal(${p.id})">
        <i class="bi bi-bag-plus-fill"></i> Agregar al carrito de consulta
      </button>
    </div>
  `;

  const related = PRODUCTS.filter(x => x.cat===p.cat && x.id!==p.id).slice(0,3);
  document.getElementById('modalRelated').innerHTML = related.length ? `
    <div class="related-title">Productos relacionados</div>
    <div class="related-grid">
      ${related.map(r => `
        <div class="related-card" onclick="openModal(${r.id})">
          <div class="related-img-area" style="background:${getGradient(r.cat)}">
            ${r.imgs && r.imgs[0]
              ? `<img src="${r.imgs[0]}" style="width:100%;height:100%;object-fit:cover" loading="lazy" onerror="this.style.display='none'">`
              : `<i class="bi ${r.icon}" style="color:var(--azul-claro)"></i>`}
          </div>
          <div class="related-name">${r.name}</div>
        </div>
      `).join('')}
    </div>
  ` : '';
}

function changeModalImg(idx) {
  if(!currentModalProduct) return;
  currentImgIdx = idx;
  const imgs = currentModalProduct.imgs || [];
  const mainImg = document.getElementById('modalMainImg');
  if(mainImg && imgs[idx]) {
    mainImg.style.opacity = '0';
    mainImg.style.transition = 'opacity 0.2s';
    setTimeout(() => {
      mainImg.src = imgs[idx];
      mainImg.style.opacity = '1';
    }, 150);
  }
  document.querySelectorAll('.modal-thumb').forEach((t,i) => t.classList.toggle('active', i===idx));
}

function selectMedida(m) {
  selectedMedida = m;
  document.querySelectorAll('.medida-btn').forEach(b => b.classList.toggle('active', b.textContent.trim()===m));
  const el = document.getElementById('medidaSelected');
  if(el) el.textContent = m;
}
function selectColor(c) {
  selectedColor = c;
  document.querySelectorAll('.color-opt').forEach(o => o.classList.toggle('active', o.style.background===c));
}
function cotizarWsp(id) {
  const p = PRODUCTS.find(x=>x.id===id);
  const msg = `Hola Full Toldos! Me interesa cotizar:%0A• *${p.name}*%0A• Medida: ${selectedMedida}%0A• Cantidad: 1 unidad%0APor favor envíame el precio.`;
  window.open(`https://wa.me/51999999999?text=${msg}`,'_blank');
}
function addCartModal(id) {
  closeModal();
  addCartById(id);
}

// ═══════════════════════════ CART ═══════════════════════════
function addCart(e, id) { e.stopPropagation(); addCartById(id); }
function addCartById(id) {
  const p = PRODUCTS.find(x=>x.id===id);
  if(!p) return;
  const ex = cart.find(i=>i.id===id);
  if(ex) ex.qty++;
  else cart.push({...p, qty:1});
  updateCartCount();
  showToast(`${p.name} agregado al carrito`);
}
function updateCartCount() {
  const total = cart.reduce((s,i)=>s+i.qty,0);
  document.getElementById('cartCount').textContent = total;
  const el = document.getElementById('cartHeadCount');
  if(el) el.textContent = `${total} producto${total!==1?'s':''}`;
}
function openCart() {
  document.getElementById('cartOverlay').classList.add('open');
  document.getElementById('cartDrawer').classList.add('open');
  renderCart();
}
function closeCart() {
  document.getElementById('cartOverlay').classList.remove('open');
  document.getElementById('cartDrawer').classList.remove('open');
}
function renderCart() {
  const body = document.getElementById('cartItems');
  const foot = document.getElementById('cartFoot');
  if(!cart.length) {
    body.innerHTML = `<div class="cart-empty"><i class="bi bi-bag"></i><p>Tu carrito está vacío</p></div>`;
    foot.innerHTML = '';
    return;
  }
  body.innerHTML = cart.map(item => `
    <div class="cart-item">
      <div class="cart-item-icon">
        ${item.imgs&&item.imgs[0]
          ? `<img src="${item.imgs[0]}" alt="${item.name}" onerror="this.parentElement.innerHTML='<i class=\"bi ${item.icon}\" style=\"font-size:1.4rem;color:var(--azul-claro)\"></i>'">`
          : `<i class="bi ${item.icon}" style="font-size:1.4rem;color:var(--azul-claro)"></i>`}
      </div>
      <div style="flex:1;min-width:0">
        <div class="cart-item-cat">${item.catName}</div>
        <div class="cart-item-nm">${item.name}</div>
        <div class="cart-qty-row">
          <button class="qty-btn" onclick="changeQty(${item.id},-1)"><i class="bi bi-dash"></i></button>
          <span class="qty-num">${item.qty}</span>
          <button class="qty-btn" onclick="changeQty(${item.id},1)"><i class="bi bi-plus"></i></button>
          <button class="cart-del" onclick="removeItem(${item.id})"><i class="bi bi-trash3"></i></button>
        </div>
      </div>
    </div>
  `).join('');
  const total = cart.reduce((s,i)=>s+i.qty,0);
  foot.innerHTML = `
    <div class="cart-total-row">
      <span class="cart-total-lbl">Total ítems</span>
      <span class="cart-total-val">${total}</span>
    </div>
    <button class="btn-checkout-wsp" onclick="sendCartWsp()">
      <i class="bi bi-whatsapp"></i> Cotizar todo por WhatsApp
    </button>
    <button class="btn-clear-cart" onclick="clearCart()">Vaciar carrito</button>
  `;
}
function changeQty(id, d) {
  const item = cart.find(i=>i.id===id);
  if(!item) return;
  item.qty += d;
  if(item.qty <= 0) cart = cart.filter(i=>i.id!==id);
  updateCartCount(); renderCart();
}
function removeItem(id) { cart = cart.filter(i=>i.id!==id); updateCartCount(); renderCart(); }
function clearCart() { cart = []; updateCartCount(); renderCart(); }
function sendCartWsp() {
  const lines = cart.map(i=>`• *${i.name}* (x${i.qty})`).join('%0A');
  window.open(`https://wa.me/51999999999?text=Hola Full Toldos! Deseo cotizar:%0A${lines}`,'_blank');
}

// ═══════════════════════════ MATERIALS ═══════════════════════════
function buildMaterials() {
  document.getElementById('matGrid').innerHTML = MATS.map(m=>`
    <div class="col-lg-4 col-md-6">
      <div class="mat-card">
        <i class="bi ${m.icon} mat-icon"></i>
        <div class="mat-name">${m.name}</div>
        <div class="mat-sub">${m.sub}</div>
        <div class="mat-desc">${m.desc}</div>
        <div class="mat-tags">${m.tags.map(t=>`<span class="mat-tag">${t}</span>`).join('')}</div>
      </div>
    </div>
  `).join('');
}

// ═══════════════════════════ WHY ═══════════════════════════
function buildWhy() {
  document.getElementById('whyGrid').innerHTML = WHY.map(w=>`
    <div class="col-lg-4 col-md-6">
      <div class="why-card">
        <div class="why-icon-wrap"><i class="bi ${w.icon}"></i></div>
        <div class="why-title">${w.title}</div>
        <div class="why-desc">${w.desc}</div>
      </div>
    </div>
  `).join('');
}

// ═══════════════════════════ CONTACT ═══════════════════════════
// buildContact eliminado (sección de cotización removida)

// ═══════════════════════════ FOOTER ═══════════════════════════
function buildFooter() {
  document.getElementById('footerProd').innerHTML = CATS.slice(0,5).map(c=>`
    <li><a href="#productos" onclick="filterProducts('${c.id}')"><i class="bi bi-chevron-right"></i>${c.name}</a></li>`).join('');
  document.getElementById('footerMat').innerHTML = [
    'Polietileno HD','Lona PVC','Lona Taiwan','Mallas PVC','Acero Galvanizado','Insumos Costura'
  ].map(m=>`<li><a href="#materiales"><i class="bi bi-chevron-right"></i>${m}</a></li>`).join('');
}

// ═══════════════════════════ TOAST ═══════════════════════════
function showToast(msg) {
  const t = document.getElementById('toastFt');
  document.getElementById('toastMsg').textContent = msg;
  t.classList.add('show');
  setTimeout(()=>t.classList.remove('show'), 2800);
}
function submitQuote() { showToast('¡Cotización enviada! Te contactaremos pronto.'); }

// ═══════════════════════════ CATEGORY PAGE ═══════════════════════════
const CAT_DESCS = {
  toldos:'Descubre nuestra línea de toldos publicitarios fabricados con aluminio anodizado de alta resistencia y lona PVC de primera calidad. Diseño, fabricación e instalación a medida para tu negocio.',
  carpas:'Carpas armables a dos aguas y techo plano para ferias, eventos y exposiciones. Estructura de acero galvanizado, lona PVC resistente. Disponibles desde 2×2 hasta 5×5 metros.',
  banderas:'Banderas publicitarias tipo pluma y gota con sublimación full color. Alta resistencia al viento, mástil de fibra de vidrio. Personalizadas con tu marca en tiempo récord.',
  inflables:'Inflables publicitarios en cualquier forma y tamaño. Tela Oxford de alta resistencia, impresión digital full color, ventilador incluido. El mayor impacto visual para tu marca.',
  juegos:'Juegos inflables para eventos y campañas de branding. Seguros, certificados y personalizables. Perfectos para activaciones de marca y espacios recreativos.',
  modulos:'Módulos publicitarios armables para ferias, stands y exposiciones. Sistema modular sin tornillos, fácil transporte y montaje rápido con impresión en alta resolución.',
  terminados:'Coberturas, estructuras y accesorios listos para usar. Fabricación exacta a medida con los mejores materiales. Entrega rápida en todo Lima y provincias.',
  lonas:'Lonas PVC, mallas y textiles técnicos para coberturas y publicidad. Venta por metro o en rollo. La mayor variedad de materiales para toldos y carpas.',
};

let cpCat = 'all', cpSearch = '', cpPerPage = 9, cpSort = 'default', cpGridCols = 3;
let cpBadges = [], cpColors = [], cpMedida = '';

const COLOR_NAMES = {
  '#1565c0':'Azul','#c62828':'Rojo','#2e7d32':'Verde',
  '#f57f17':'Naranja','#ffffff':'Blanco','#212121':'Negro','#9e9e9e':'Gris'
};

function openCatPage(catId) {
  cpCat = catId || 'all';
  cpSearch = ''; cpPerPage = 9; cpSort = 'default'; cpGridCols = 3;
  cpBadges = []; cpColors = []; cpMedida = '';
  document.getElementById('mainView').style.display = 'none';
  document.getElementById('catPageView').style.display = 'block';
  const si = document.getElementById('catSearchInput');
  if(si) si.value = '';
  buildSidebarCats();
  renderCatPage();
  window.scrollTo({top:0, behavior:'smooth'});
}

function closeCatPage() {
  document.getElementById('catPageView').style.display = 'none';
  document.getElementById('mainView').style.display = 'block';
  window.scrollTo({top:0, behavior:'smooth'});
}

function buildSidebarCats() {
  const list = document.getElementById('sidebarCatLinks');
  if(!list) return;
  list.innerHTML = `
    <a class="sidebar-cat-link" data-catid="all" onclick="openCatPage('all')">
      Todos los Productos
      <span class="sidebar-cat-count">${PRODUCTS.length}</span>
    </a>
  ` + CATS.map(c => `
    <a class="sidebar-cat-link" data-catid="${c.id}" onclick="openCatPage('${c.id}')">
      ${c.name}
      <span class="sidebar-cat-count">${c.count}</span>
    </a>
  `).join('');
}

function renderCatPage() {
  const cat = CATS.find(c => c.id === cpCat);
  const catName = cat ? cat.name : 'Todos los Productos';
  document.getElementById('catPageTitle').textContent = catName;
  document.getElementById('catBreadcrumb').innerHTML =
    `<a href="#" onclick="closeCatPage();return false;">Inicio</a> / ${catName}`;
  const desc = CAT_DESCS[cpCat] || `Explora todos los productos de la categoría ${catName}.`;
  document.getElementById('catDescBox').textContent = desc;
  document.querySelectorAll('.sidebar-cat-link').forEach(el => {
    el.classList.toggle('active', el.dataset.catid === cpCat);
  });

  let prods = cpCat === 'all' ? [...PRODUCTS] : PRODUCTS.filter(p => p.cat === cpCat);
  if(cpSearch) {
    const q = cpSearch.toLowerCase();
    prods = prods.filter(p => p.name.toLowerCase().includes(q) || p.catName.toLowerCase().includes(q));
  }
  if(cpBadges.length) prods = prods.filter(p => p.badge && cpBadges.includes(p.badge));
  if(cpColors.length) prods = prods.filter(p => p.colores && p.colores.some(c => cpColors.includes(c)));
  if(cpMedida)        prods = prods.filter(p => p.medidas && p.medidas.includes(cpMedida));
  if(cpSort === 'name-asc') prods.sort((a,b) => a.name.localeCompare(b.name));
  if(cpSort === 'name-desc') prods.sort((a,b) => b.name.localeCompare(a.name));

  const shown = prods.slice(0, cpPerPage);
  const grid = document.getElementById('catProductGrid');
  grid.className = `cat-page-grid cat-grid-${cpGridCols}`;

  if(!shown.length) {
    grid.innerHTML = `<div class="cat-no-results"><i class="bi bi-search" style="font-size:2.5rem;display:block;margin-bottom:14px;"></i>Sin resultados${cpSearch?' para "'+cpSearch+'"':''}</div>`;
  } else {
    grid.innerHTML = shown.map(p => `
      <div class="prod-card" onclick="openModal(${p.id})">
        <div class="prod-img-area" style="background:${getGradient(p.cat)}">
          ${p.imgs && p.imgs[0]
            ? `<img src="${p.imgs[0]}" alt="${p.name}" class="prod-img-real" loading="lazy"
                onerror="this.style.display='none';this.parentElement.querySelector('.prod-img-placeholder').style.display='flex'">
               <div class="prod-img-placeholder" style="display:none;position:absolute;inset:0"><i class="bi ${p.icon}" style="font-size:3rem;color:rgba(21,101,192,0.35)"></i></div>`
            : `<div class="prod-img-placeholder"><i class="bi ${p.icon}" style="font-size:3rem;color:rgba(21,101,192,0.35)"></i></div>`}
          <div class="prod-badge-wrap">
            ${p.badge==='new'?'<span class="prod-badge badge-new">Nuevo</span>':''}
            ${p.badge==='hot'?'<span class="prod-badge badge-hot">Popular</span>':''}
            ${p.badge==='sale'?'<span class="prod-badge badge-sale">Oferta</span>':''}
          </div>
          <button class="prod-eye-btn" onclick="event.stopPropagation();openProductPage(${p.id})" title="Ver producto completo"><i class="bi bi-eye"></i></button>
        </div>
        <div class="prod-body">
          <div class="prod-cat-tag">${p.catName}</div>
          <div class="prod-name">${p.name}</div>
          ${getStarsHTML(p)}
          <div class="prod-desc">${p.desc.substring(0,95)}...</div>
          <div class="prod-footer">
            <div>
              <div class="prod-price" style="font-size:1.1rem;color:var(--azul-claro);">Consultar precio</div>
              <div class="prod-price-note">Fabricación a medida</div>
            </div>
            ${getWspCardBtn(p)}
          </div>
        </div>
      </div>
    `).join('');
  }
  document.getElementById('catLoadMoreArea').style.display = (cpPerPage < prods.length) ? 'block' : 'none';
  document.querySelectorAll('.tsb').forEach(b => b.classList.toggle('active', parseInt(b.textContent) === cpPerPage));
  document.querySelectorAll('.tvb').forEach((b,i) => b.classList.toggle('active', [3,4,2][i] === cpGridCols));
  const sortSel = document.querySelector('.toolbar-sort-sel');
  if(sortSel) sortSel.value = cpSort;
  buildSidebarFilters();
  // Productos de otras categorías como sugerencias
  const shownIds = (cpCat==='all' ? PRODUCTS : PRODUCTS.filter(p=>p.cat===cpCat))
    .slice(0, cpPerPage).map(p=>p.id);
  renderTambienInteresa('catTambienSection', cpCat==='all' ? null : cpCat, shownIds);
}

function doCatSearch() {
  cpSearch = (document.getElementById('catSearchInput').value || '').trim();
  cpPerPage = 9;
  renderCatPage();
}
function setCatPerPage(n) { cpPerPage = n; renderCatPage(); }
function setCatSort(val) { cpSort = val; renderCatPage(); }
function setCatGrid(cols) { cpGridCols = cols; renderCatPage(); }
function catLoadMore() { cpPerPage += 6; renderCatPage(); }

// ═══════════════════════════ SIDEBAR FILTROS ═══════════════════════════
function buildSidebarFilters() {
  const box = document.getElementById('sidebarFiltersBox');
  if(!box) return;

  // Base products for counting (sin badge/color filters aplicados)
  let base = cpCat === 'all' ? [...PRODUCTS] : PRODUCTS.filter(p => p.cat === cpCat);
  if(cpSearch) {
    const q = cpSearch.toLowerCase();
    base = base.filter(p => p.name.toLowerCase().includes(q) || p.catName.toLowerCase().includes(q));
  }

  const bCount = { new:0, hot:0, sale:0 };
  base.forEach(p => { if(p.badge) bCount[p.badge]++; });

  const allColors = [...new Set(base.flatMap(p => p.colores.filter(c => c[0]==='#')))];

  const activeCount = cpBadges.length + cpColors.length + (cpMedida ? 1 : 0);
  const badgeDef = [
    {key:'new', label:'Nuevo', cls:'badge-new'},
    {key:'hot', label:'Popular', cls:'badge-hot'},
    {key:'sale', label:'Oferta', cls:'badge-sale'},
  ];

  const badgeHTML = badgeDef.filter(b => bCount[b.key] > 0).map(b => `
    <div class="filter-check-item ${cpBadges.includes(b.key)?'active':''}" onclick="toggleCpBadge('${b.key}')">
      <div class="filter-check-box"></div>
      <span class="filter-check-lbl">${b.label}</span>
      <span class="filter-count">${bCount[b.key]}</span>
    </div>
  `).join('');

  const colorHTML = allColors.map(c => `
    <div class="filter-color-dot ${cpColors.includes(c)?'active':''}"
      style="background:${c}" data-c="${c}"
      title="${COLOR_NAMES[c]||c}"
      onclick="toggleCpColor('${c}')"></div>
  `).join('');

  box.innerHTML = `
    <div class="sidebar-title">
      Filtros
      ${activeCount > 0 ? `<span class="sidebar-filter-badge">${activeCount}</span>` : ''}
    </div>
    ${cpMedida ? `
      <div class="filter-medida-active">
        <span><i class="bi bi-rulers"></i> Medida: <strong>${cpMedida}</strong></span>
        <button onclick="clearMedidaFilter()" title="Quitar filtro">✕</button>
      </div>` : ''}
    ${badgeHTML ? `
      <div class="filter-group">
        <div class="filter-group-lbl">Estado del producto</div>
        ${badgeHTML}
      </div>
    ` : ''}
    ${allColors.length ? `
      ${badgeHTML ? '<div class="filter-sep"></div>' : ''}
      <div class="filter-group">
        <div class="filter-group-lbl">Color disponible</div>
        <div class="filter-color-grid">${colorHTML}</div>
      </div>
    ` : ''}
    ${activeCount > 0 ? `
      <div class="filter-sep"></div>
      <button class="filter-clear-btn" onclick="clearAllFilters()">
        <i class="bi bi-x-circle"></i> Limpiar filtros
      </button>
    ` : ''}
  `;
}

function toggleCpBadge(badge) {
  cpBadges = cpBadges.includes(badge) ? cpBadges.filter(b=>b!==badge) : [...cpBadges, badge];
  cpPerPage = 9;
  renderCatPage();
}
function toggleCpColor(color) {
  cpColors = cpColors.includes(color) ? cpColors.filter(c=>c!==color) : [...cpColors, color];
  cpPerPage = 9;
  renderCatPage();
}
function clearAllFilters() {
  cpBadges = []; cpColors = []; cpMedida = '';
  renderCatPage();
}
function clearMedidaFilter() {
  cpMedida = '';
  renderCatPage();
}

// ═══════════════════════════ POR MEDIDA ═══════════════════════════
function buildMedidaDropdown() {
  const menu = document.getElementById('navMedidaMenu');
  if(!menu) return;
  menu.classList.add('nav-medida-menu');

  // Recoger todas las medidas únicas con formato NxNm y contar productos
  const medidaCount = {};
  PRODUCTS.forEach(p => {
    (p.medidas || []).forEach(m => {
      // Solo medidas rectangulares tipo "2x2m", "3x3m" etc.
      if(/^\d+x\d+m$/.test(m)) {
        medidaCount[m] = (medidaCount[m] || 0) + 1;
      }
    });
  });

  // Ordenar por número de productos, luego por tamaño
  const sorted = Object.entries(medidaCount)
    .sort((a, b) => b[1] - a[1] || a[0].localeCompare(b[0]))
    .slice(0, 12); // máximo 12 medidas

  const btns = sorted.map(([m, cnt]) => `
    <div class="medida-nav-btn" onclick="openCatByMedida('${m}');closeAllDropdowns()">
      <span class="mnb-size">${m}</span>
      <span class="mnb-count">${cnt} producto${cnt !== 1 ? 's' : ''}</span>
    </div>
  `).join('');

  menu.innerHTML = `
    <span class="nav-mega-section-title" style="padding:0 0 2px 0;display:block">
      <i class="bi bi-rulers" style="margin-right:6px;color:var(--azul-claro)"></i>Buscar por Medida
    </span>
    <div class="medida-nav-grid">${btns}</div>
    <div class="nav-drop-divider"></div>
    <div class="medida-cta" onclick="closeAllDropdowns();openCatPage('all')">
      <i class="bi bi-pencil-square" style="color:var(--azul-claro)"></i>
      ¿Otra medida? <strong>Ver catálogo completo →</strong>
    </div>
  `;
}

function openCatByMedida(medida) {
  cpMedida  = medida;
  cpCat     = 'all';
  cpBadges  = []; cpColors = [];
  cpSearch  = '';
  cpPerPage = 9; cpSort = 'default'; cpGridCols = 3;
  document.getElementById('mainView').style.display    = 'none';
  document.getElementById('catPageView').style.display = 'block';
  document.getElementById('prodPageView').style.display= 'none';
  document.getElementById('galPageView').style.display = 'none';
  const si = document.getElementById('catSearchInput');
  if(si) si.value = '';
  buildSidebarCats();
  renderCatPage();
  window.scrollTo({top:0, behavior:'smooth'});
}

// ═══════════════════════════ NAV DROPDOWNS ═══════════════════════════
function buildNavDropdowns() {
  const catMenu = document.getElementById('navCatMenu');
  if(catMenu) {
    catMenu.classList.add('nav-mega-menu');

    // Featured products: badge new or hot, up to 3
    const featured = PRODUCTS.filter(p => p.badge === 'new' || p.badge === 'hot').slice(0, 3);

    const catLinks = CATS.map(c => `
      <a href="#" class="nav-drop-item" onclick="event.preventDefault();closeAllDropdowns();openCatPage('${c.id}')">
        <i class="bi ${c.icon} nav-drop-icon"></i>
        <span>${c.name}</span>
        <span class="nav-drop-count">${c.count}</span>
      </a>
    `).join('');

    const featCards = featured.map(p => `
      <div class="nav-feat-card" onclick="closeAllDropdowns();openProductPage(${p.id})">
        <div class="nav-feat-img-wrap" style="background:${getGradient(p.cat)}">
          ${p.imgs && p.imgs[0]
            ? `<img src="${p.imgs[0]}" alt="${p.name}" class="nav-feat-img" loading="lazy"
                onerror="this.parentElement.innerHTML='<div class=nav-feat-img-ph><i class=bi\\ ${p.icon.replace(/\\/g,'')} style=color:rgba(21,101,192,0.4)></i></div>'">`
            : `<div class="nav-feat-img-ph"><i class="bi ${p.icon}" style="color:rgba(21,101,192,0.4)"></i></div>`}
        </div>
        <div class="nav-feat-body">
          ${p.badge==='new' ? '<span class="nav-feat-nbadge badge-new">Nuevo</span>' : ''}
          ${p.badge==='hot' ? '<span class="nav-feat-nbadge badge-hot">Popular</span>' : ''}
          <div class="nav-feat-name">${p.name}</div>
          <div class="nav-feat-cat">${p.catName}</div>
        </div>
      </div>
    `).join('');

    catMenu.innerHTML = `
      <div class="nav-mega-inner">
        <div class="nav-mega-left">
          <span class="nav-mega-section-title">Categorías</span>
          ${catLinks}
          <div class="nav-drop-divider"></div>
          <a href="#" class="nav-drop-all" onclick="event.preventDefault();closeAllDropdowns();openCatPage('all')">
            Ver todos los productos <i class="bi bi-arrow-right"></i>
          </a>
        </div>
        <div class="nav-mega-right">
          <span class="nav-mega-section-title">Productos Destacados</span>
          <div class="nav-feat-grid">${featCards}</div>
        </div>
      </div>
    `;
  }

  const prodMenu = document.getElementById('navProdMenu');
  if(prodMenu) {
    prodMenu.classList.add('nav-prods-mega');

    // 5 productos destacados: primero los que tienen badge, luego rellena
    let featProds = PRODUCTS.filter(p => p.badge);
    const rest = PRODUCTS.filter(p => !p.badge);
    let i = 0;
    while(featProds.length < 5 && i < rest.length) { featProds.push(rest[i++]); }
    featProds = featProds.slice(0, 5);

    const cards = featProds.map(p => `
      <div class="nav-feat-card" onclick="closeAllDropdowns();openProductPage(${p.id})">
        <div class="nav-feat-img-wrap" style="background:${getGradient(p.cat)}">
          ${p.imgs && p.imgs[0]
            ? `<img src="${p.imgs[0]}" alt="${p.name}" class="nav-feat-img" loading="lazy"
                onerror="this.style.display='none'">`
            : `<div class="nav-feat-img-ph"><i class="bi ${p.icon}" style="color:rgba(0,0,0,0.25)"></i></div>`}
          ${p.badge === 'new'  ? '<span style="position:absolute;top:6px;left:6px;font-size:0.48rem;font-weight:700;letter-spacing:1px;text-transform:uppercase;background:#e8eaf6;color:#1949c9;padding:2px 7px;border-radius:10px;font-family:var(--font-mono)">NUEVO</span>' : ''}
          ${p.badge === 'hot'  ? '<span style="position:absolute;top:6px;left:6px;font-size:0.48rem;font-weight:700;letter-spacing:1px;text-transform:uppercase;background:#fce4ec;color:#c62828;padding:2px 7px;border-radius:10px;font-family:var(--font-mono)">POPULAR</span>' : ''}
          ${p.badge === 'sale' ? '<span style="position:absolute;top:6px;left:6px;font-size:0.48rem;font-weight:700;letter-spacing:1px;text-transform:uppercase;background:#e8eaf6;color:#1949c9;padding:2px 7px;border-radius:10px;font-family:var(--font-mono)">OFERTA</span>' : ''}
        </div>
        <div class="nav-feat-body">
          <div class="nav-feat-name">${p.name}</div>
          <div class="nav-feat-cat">${p.catName}</div>
        </div>
      </div>
    `).join('');

    prodMenu.innerHTML = `
      <div class="nav-prods-header">
        <span class="nav-mega-section-title" style="padding:0;margin:0">Productos Destacados</span>
        <a href="#" class="nav-drop-all" style="border:none;padding:0;font-size:0.62rem;"
          onclick="event.preventDefault();closeAllDropdowns();openCatPage('all')">
          Ver todos <i class="bi bi-arrow-right"></i>
        </a>
      </div>
      <div class="nav-feat-grid-5">${cards}</div>
    `;
  }

  // Click toggle
  document.querySelectorAll('.nav-dropdown-toggle').forEach(toggle => {
    toggle.addEventListener('click', function(e) {
      e.preventDefault();
      const parent = this.closest('.nav-dropdown');
      const isOpen = parent.classList.contains('open');
      closeAllDropdowns();
      if(!isOpen) parent.classList.add('open');
    });
  });

  document.addEventListener('click', function(e) {
    if(!e.target.closest('.nav-dropdown')) closeAllDropdowns();
  });
}

function closeAllDropdowns() {
  document.querySelectorAll('.nav-dropdown.open').forEach(d => d.classList.remove('open'));
}

// ═══════════════════════════ SCROLL TOP ═══════════════════════════
window.addEventListener('scroll', () => {
  document.getElementById('scrollTopBtn').classList.toggle('visible', window.scrollY > 400);
});

// ═══════════════════════════ GALERÍA ═══════════════════════════
let galleryItems = [];
let lightboxIdx  = 0;
let galActiveCat = 'all';
let prevGalView  = 'main';

function buildGalleryItems() {
  galleryItems = [];
  PRODUCTS.forEach(p => {
    if(p.imgs && p.imgs.length) {
      p.imgs.forEach(src => {
        galleryItems.push({ src, name: p.name, cat: p.catName, catId: p.cat, prodId: p.id });
      });
    }
  });
}

function openGallery(catId) {
  galActiveCat = catId || 'all';
  prevGalView = document.getElementById('catPageView').style.display !== 'none' ? 'category'
              : document.getElementById('prodPageView').style.display !== 'none' ? 'product' : 'main';
  buildGalleryItems();
  document.getElementById('mainView').style.display    = 'none';
  document.getElementById('catPageView').style.display = 'none';
  document.getElementById('prodPageView').style.display= 'none';
  document.getElementById('galPageView').style.display = 'block';
  renderGallery();
  window.scrollTo({top:0, behavior:'smooth'});
}

function closeGallery() {
  document.getElementById('galPageView').style.display = 'none';
  if(prevGalView === 'category')     document.getElementById('catPageView').style.display = 'block';
  else if(prevGalView === 'product') document.getElementById('prodPageView').style.display= 'block';
  else                               document.getElementById('mainView').style.display    = 'block';
  window.scrollTo({top:0, behavior:'smooth'});
}

function renderGallery() {
  // Filtros
  const allCats = [{id:'all', name:'Todas las fotos'}, ...CATS];
  document.getElementById('galFilterBar').innerHTML = allCats.map(c => `
    <button class="filter-btn ${c.id===galActiveCat?'active':''}" onclick="galSetCat('${c.id}')">
      ${c.id==='all'?'<i class="bi bi-images"></i> ':''}${c.name}
    </button>`).join('');

  // Grid
  const items = galActiveCat==='all'
    ? galleryItems
    : galleryItems.filter(i => i.catId === galActiveCat);

  const grid = document.getElementById('galGrid');
  if(!items.length) {
    grid.innerHTML = '<div style="grid-column:1/-1;text-align:center;padding:60px 0;font-family:var(--font-mono);color:#aaa;letter-spacing:2px;font-size:0.75rem;">SIN FOTOS EN ESTA CATEGORÍA</div>';
    return;
  }
  grid.innerHTML = items.map(item => {
    const gi = galleryItems.indexOf(item);
    return `
      <div class="gal-item" onclick="openLightbox(${gi})">
        <img src="${item.src}" alt="${item.name}" class="gal-img" loading="lazy"
          onerror="this.closest('.gal-item').style.display='none'">
        <div class="gal-overlay">
          <div class="gal-zoom-icon"><i class="bi bi-zoom-in"></i></div>
          <div class="gal-item-name">${item.name}</div>
          <div class="gal-item-cat">${item.cat}</div>
        </div>
      </div>`;
  }).join('');
  // Sugerencias al pie de la galería
  renderTambienInteresa('galTambienSection', galActiveCat === 'all' ? null : galActiveCat, []);
}

function galSetCat(catId) {
  galActiveCat = catId;
  renderGallery();
  window.scrollTo({top: document.getElementById('galFilterBar').offsetTop - 160, behavior:'smooth'});
}

// ─── Lightbox ───
function openLightbox(idx) {
  lightboxIdx = idx;
  updateLightbox();
  document.getElementById('lightboxOverlay').classList.add('open');
  document.body.style.overflow = 'hidden';
}

function closeLightbox() {
  document.getElementById('lightboxOverlay').classList.remove('open');
  document.body.style.overflow = '';
}

function updateLightbox() {
  const item = galleryItems[lightboxIdx];
  if(!item) return;
  const img = document.getElementById('lightboxImg');
  img.style.opacity = '0';
  setTimeout(() => { img.src = item.src; img.style.opacity = '1'; }, 140);
  document.getElementById('lightboxName').textContent = item.name;
  document.getElementById('lightboxCat').textContent  = item.cat;
  document.getElementById('lightboxViewBtn').dataset.pid = item.prodId;
  document.querySelector('.lightbox-prev').disabled = lightboxIdx <= 0;
  document.querySelector('.lightbox-next').disabled = lightboxIdx >= galleryItems.length - 1;
}

function lightboxNav(dir) {
  const n = lightboxIdx + dir;
  if(n < 0 || n >= galleryItems.length) return;
  lightboxIdx = n;
  updateLightbox();
}

function lightboxGoProduct() {
  const pid = parseInt(document.getElementById('lightboxViewBtn').dataset.pid);
  closeLightbox();
  openProductPage(pid);
}

// Teclado para lightbox
document.addEventListener('keydown', e => {
  if(!document.getElementById('lightboxOverlay').classList.contains('open')) return;
  if(e.key === 'ArrowLeft')  lightboxNav(-1);
  if(e.key === 'ArrowRight') lightboxNav(1);
  if(e.key === 'Escape')     closeLightbox();
});

// ═══════════════════════════ TAMBIÉN PODRÍA INTERESARTE ═══════════════════════════
function renderTambienInteresa(containerId, excludeCatId, excludeIds) {
  const el = document.getElementById(containerId);
  if(!el) return;
  excludeIds = excludeIds || [];
  const limit = 8;
  let pool = PRODUCTS.filter(p =>
    (!excludeCatId || p.cat !== excludeCatId) && !excludeIds.includes(p.id)
  );
  const badged  = pool.filter(p => p.badge);
  const rest    = pool.filter(p => !p.badge).sort(() => Math.random() - 0.5);
  const selected = [...badged, ...rest].slice(0, limit);
  if(!selected.length) { el.innerHTML = ''; return; }

  const scrollId = 'tb_' + containerId;
  el.innerHTML = `
    <div class="tambien-section">
      <div class="tambien-header">
        <div class="tambien-title"><i class="bi bi-stars"></i> También podría interesarte</div>
        <span class="tambien-count">${selected.length} sugerencias</span>
      </div>
      <div class="tambien-scroll-wrap">
        <button class="tambien-arrow tambien-arrow-left" onclick="tambienNav('${scrollId}',-1)">
          <i class="bi bi-chevron-left"></i>
        </button>
        <div class="tambien-scroll" id="${scrollId}">
          ${selected.map(p => `
            <div class="prod-card" onclick="openProductPage(${p.id})">
              <div class="prod-img-area" style="background:${getGradient(p.cat)}">
                ${p.imgs && p.imgs[0]
                  ? `<img src="${p.imgs[0]}" alt="${p.name}" class="prod-img-real" loading="lazy"
                      onerror="this.style.display='none';this.parentElement.querySelector('.prod-img-placeholder').style.display='flex'">
                     <div class="prod-img-placeholder" style="display:none;position:absolute;inset:0">
                       <i class="bi ${p.icon}" style="font-size:2.5rem;color:rgba(0,0,0,0.2)"></i></div>`
                  : `<div class="prod-img-placeholder">
                       <i class="bi ${p.icon}" style="font-size:2.5rem;color:rgba(0,0,0,0.2)"></i></div>`}
                <div class="prod-badge-wrap">
                  ${p.badge==='new'?'<span class="prod-badge badge-new">Nuevo</span>':''}
                  ${p.badge==='hot'?'<span class="prod-badge badge-hot">Popular</span>':''}
                  ${p.badge==='sale'?'<span class="prod-badge badge-sale">Oferta</span>':''}
                </div>
                <button class="prod-eye-btn" onclick="event.stopPropagation();openProductPage(${p.id})" title="Ver producto completo"><i class="bi bi-eye"></i></button>
              </div>
              <div class="prod-body">
                <div class="prod-cat-tag">${p.catName}</div>
                <div class="prod-name">${p.name}</div>
                ${getStarsHTML(p)}
                <div class="prod-desc">${p.desc.substring(0,75)}...</div>
                <div class="prod-footer">
                  <div>${getPriceHTML(p)}</div>
                  <span onclick="event.stopPropagation()">${getWspCardBtn(p)}</span>
                </div>
              </div>
            </div>`).join('')}
        </div>
        <button class="tambien-arrow tambien-arrow-right" onclick="tambienNav('${scrollId}',1)">
          <i class="bi bi-chevron-right"></i>
        </button>
      </div>
    </div>`;

  initTambienAutoScroll(scrollId);
}

function tambienNav(id, dir) {
  const el = document.getElementById(id);
  if(el) el.scrollBy({ left: dir * 260, behavior: 'smooth' });
}

function initTambienAutoScroll(id) {
  const el = document.getElementById(id);
  if(!el) return;
  let timer = setInterval(() => scrollTambien(el), 3000);
  el.addEventListener('mouseenter', () => clearInterval(timer));
  el.addEventListener('mouseleave', () => {
    clearInterval(timer);
    timer = setInterval(() => scrollTambien(el), 3000);
  });
}

function scrollTambien(el) {
  const max = el.scrollWidth - el.clientWidth;
  if(el.scrollLeft >= max - 5) {
    el.scrollTo({ left: 0, behavior: 'smooth' });
  } else {
    el.scrollBy({ left: 260, behavior: 'smooth' });
  }
}

// ═══════════════════════════ PRODUCT IMAGE ZOOM ═══════════════════════════
let _zoomListeners = {};

function initProductZoom() {
  if (window.innerWidth < 992) return;
  const imgEl = document.getElementById('prodMainImg');
  const imgArea = document.querySelector('#prodPageView .prod-main-img-area');
  if (!imgEl || !imgArea) return;

  let lens = imgArea.querySelector('.zoom-lens');
  if (!lens) {
    lens = document.createElement('div');
    lens.className = 'zoom-lens';
    imgArea.appendChild(lens);
  }

  let result = document.getElementById('zoomResult');
  if (!result) {
    result = document.createElement('div');
    result.id = 'zoomResult';
    document.body.appendChild(result);
  }
  result.style.display = 'none';

  const LENS_W = 110, LENS_H = 110;
  const RES_W = 380, RES_H = 380;

  function onMove(e) {
    const rect = imgEl.getBoundingClientRect();
    let lx = e.clientX - rect.left - LENS_W / 2;
    let ly = e.clientY - rect.top  - LENS_H / 2;
    lx = Math.max(0, Math.min(lx, rect.width  - LENS_W));
    ly = Math.max(0, Math.min(ly, rect.height - LENS_H));
    lens.style.left = lx + 'px';
    lens.style.top  = ly + 'px';

    let rx = rect.right + 16;
    let ry = rect.top;
    if (rx + RES_W > window.innerWidth - 10) rx = rect.left - RES_W - 16;
    ry = Math.max(10, Math.min(ry, window.innerHeight - RES_H - 10));
    result.style.left   = rx + 'px';
    result.style.top    = ry + 'px';
    result.style.width  = RES_W + 'px';
    result.style.height = RES_H + 'px';

    const cx = RES_W / LENS_W, cy = RES_H / LENS_H;
    result.style.backgroundImage    = `url('${imgEl.src}')`;
    result.style.backgroundSize     = `${rect.width * cx}px ${rect.height * cy}px`;
    result.style.backgroundPosition = `-${lx * cx}px -${ly * cy}px`;
  }

  function onEnter() { lens.style.display = 'block'; result.style.display = 'block'; }
  function onLeave() { lens.style.display = 'none';  result.style.display = 'none'; }

  imgArea.removeEventListener('mousemove',  _zoomListeners.move);
  imgArea.removeEventListener('mouseenter', _zoomListeners.enter);
  imgArea.removeEventListener('mouseleave', _zoomListeners.leave);
  _zoomListeners = { move: onMove, enter: onEnter, leave: onLeave };
  imgArea.addEventListener('mousemove',  onMove);
  imgArea.addEventListener('mouseenter', onEnter);
  imgArea.addEventListener('mouseleave', onLeave);
}

function cleanupProductZoom() {
  const r = document.getElementById('zoomResult');
  if (r) r.style.display = 'none';
}

// ═══════════════════════════ MOBILE MENU ═══════════════════════════
function buildMobileMenu() {
  const nav = document.getElementById('mobNav');
  if(!nav) return;

  const links = [
    { label:'Inicio',     href:'#inicio',     icon:'bi-house-fill' },
    { label:'Materiales', href:'#materiales', icon:'bi-layers-fill' },
    { label:'Contacto',   href:'#contacto',   icon:'bi-envelope-fill' },
  ];

  let html = '';

  // Categorías accordion
  html += `<div class="mob-nav-link" onclick="mobToggleSub('mobSubCats',this)">
    <span><i class="bi bi-grid-fill" style="margin-right:8px;color:var(--azul-claro)"></i>Categorías</span>
    <i class="bi bi-chevron-right mob-arrow"></i>
  </div>
  <div class="mob-sub" id="mobSubCats">
    <div class="mob-sub-link" onclick="closeMobileMenu();openCatPage('all')">
      <i class="bi bi-grid-3x3-gap-fill"></i><span>Todos los productos</span>
      <span class="mob-sub-count">${PRODUCTS.length}</span>
    </div>
    ${CATS.map(c=>`
      <div class="mob-sub-link" onclick="closeMobileMenu();openCatPage('${c.id}')">
        <i class="bi ${c.icon}"></i><span>${c.name}</span>
        <span class="mob-sub-count">${c.count}</span>
      </div>`).join('')}
  </div>`;

  // Productos accordion
  html += `<div class="mob-nav-link" onclick="mobToggleSub('mobSubProds',this)">
    <span><i class="bi bi-box-seam-fill" style="margin-right:8px;color:var(--azul-claro)"></i>Productos</span>
    <i class="bi bi-chevron-right mob-arrow"></i>
  </div>
  <div class="mob-sub" id="mobSubProds">
    ${PRODUCTS.map(p=>`
      <div class="mob-sub-link" onclick="closeMobileMenu();openProductPage(${p.id})">
        <i class="bi ${p.icon}"></i><span>${p.name}</span>
      </div>`).join('')}
  </div>`;

  // Por Medida accordion
  const medidaCount = {};
  PRODUCTS.forEach(p => {
    (p.medidas||[]).forEach(m => {
      if(/^\d+x\d+m$/.test(m)) medidaCount[m] = (medidaCount[m]||0)+1;
    });
  });
  const topMedidas = Object.entries(medidaCount).sort((a,b)=>b[1]-a[1]).slice(0,8);
  html += `<div class="mob-nav-link" onclick="mobToggleSub('mobSubMedidas',this)">
    <span><i class="bi bi-rulers" style="margin-right:8px;color:var(--azul-claro)"></i>Por Medida</span>
    <i class="bi bi-chevron-right mob-arrow"></i>
  </div>
  <div class="mob-sub" id="mobSubMedidas">
    ${topMedidas.map(([m,cnt])=>`
      <div class="mob-sub-link" onclick="closeMobileMenu();openCatByMedida('${m}')">
        <i class="bi bi-rulers"></i><span>${m}</span>
        <span class="mob-sub-count">${cnt} prod.</span>
      </div>`).join('')}
  </div>`;

  // Galería
  html += `<div class="mob-nav-link" onclick="closeMobileMenu();openGallery()">
    <span><i class="bi bi-images" style="margin-right:8px;color:var(--azul-claro)"></i>Galería</span>
  </div>`;

  // Links simples
  links.forEach(l => {
    html += `<a href="${l.href}" class="mob-nav-link" onclick="closeMobileMenu()">
      <span><i class="bi ${l.icon}" style="margin-right:8px;color:var(--azul-claro)"></i>${l.label}</span>
    </a>`;
  });

  nav.innerHTML = html;
}

function mobToggleSub(id, el) {
  const sub = document.getElementById(id);
  if(!sub) return;
  const isOpen = sub.classList.contains('open');
  // Cierra todos
  document.querySelectorAll('.mob-sub.open').forEach(s => s.classList.remove('open'));
  document.querySelectorAll('.mob-nav-link.open').forEach(l => l.classList.remove('open'));
  if(!isOpen) { sub.classList.add('open'); el.classList.add('open'); }
}

function openMobileMenu() {
  document.getElementById('mobOverlay').classList.add('open');
  document.getElementById('mobDrawer').classList.add('open');
  document.body.style.overflow = 'hidden';
}

function closeMobileMenu() {
  document.getElementById('mobOverlay').classList.remove('open');
  document.getElementById('mobDrawer').classList.remove('open');
  document.body.style.overflow = '';
}

function doMobSearch() {
  const q = (document.getElementById('mobSearchInput').value || '').trim();
  if(!q) return;
  closeMobileMenu();
  cpSearch = q; cpCat = 'all'; cpBadges = []; cpColors = [];
  cpPerPage = 9; cpSort = 'default'; cpGridCols = 3;
  document.getElementById('mainView').style.display = 'none';
  document.getElementById('catPageView').style.display = 'block';
  document.getElementById('prodPageView').style.display = 'none';
  buildSidebarCats(); renderCatPage();
  window.scrollTo({top:0, behavior:'smooth'});
}

// ═══════════════════════════ NAV SEARCH ═══════════════════════════
function buildNavSearch() {
  const sel = document.getElementById('navSearchCat');
  if(!sel) return;
  CATS.forEach(c => {
    const o = document.createElement('option');
    o.value = c.id; o.textContent = c.name;
    sel.appendChild(o);
  });
}

function doNavSearch() {
  const q   = (document.getElementById('navSearchInput').value || '').trim();
  const cat = document.getElementById('navSearchCat').value || 'all';
  closeAllDropdowns();
  cpBadges = []; cpColors = [];
  cpSearch  = q;
  cpCat     = cat;
  cpPerPage = 9; cpSort = 'default'; cpGridCols = 3;
  document.getElementById('mainView').style.display    = 'none';
  document.getElementById('catPageView').style.display = 'block';
  document.getElementById('prodPageView').style.display = 'none';
  buildSidebarCats();
  renderCatPage();
  window.scrollTo({top:0, behavior:'smooth'});
}

// ═══════════════════════════ INIT ═══════════════════════════
buildTicker();
buildCats();
buildFilterTabs();
renderProducts();
buildMaterials();
buildWhy();
buildFooter();
buildNavDropdowns();
buildNavSearch();
buildMedidaDropdown();
buildMobileMenu();
startAutoSlide();
// Mover footer fuera de mainView para que aparezca en todas las páginas
const _ft = document.querySelector('.footer-main');
if(_ft) document.body.appendChild(_ft);

// Pegar navbar justo debajo del topbar (sin gap)
(function fixNavPos() {
  const tb = document.querySelector('.topbar-ft');
  const nb = document.querySelector('.navbar-ft');
  if(!tb || !nb) return;
  const apply = () => {
    const h = tb.getBoundingClientRect().height;
    nb.style.top = h + 'px';
    // Ajustar páginas al total del header
    const totalH = h + nb.getBoundingClientRect().height;
    document.querySelectorAll('.hero-section').forEach(el => el.style.marginTop = totalH + 'px');
    document.querySelectorAll('#catPageView,#prodPageView,#galPageView').forEach(el => {
      if(el.style.display !== 'none') el.style.paddingTop = totalH + 'px';
    });
  };
  apply();
  window.addEventListener('resize', apply);
})();
