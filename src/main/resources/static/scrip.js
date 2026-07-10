
// ═══════════════════════════ DATA ═══════════════════════════
const CATS = [];
const PRODUCTS = [];
const PRICES = {};

function getYouTubeId(url) {
  if (!url) return null;
  const m = url.match(/(?:youtube\.com\/(?:watch\?v=|embed\/)|youtu\.be\/)([^&?#\s]+)/);
  return m ? m[1] : null;
}

function buildVideoPlayer(url, opts) {
  const ytId = getYouTubeId(url);
  if (ytId) {
    const autoplay = opts && opts.autoplay ? '?autoplay=1' : '';
    return `<iframe src="https://www.youtube.com/embed/${ytId}${autoplay}"
              frameborder="0" allowfullscreen
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              style="width:100%;height:100%;min-height:320px;"></iframe>`;
  }
  return `<video controls ${opts && opts.autoplay ? 'autoplay' : ''}
            src="${url}" style="max-width:100%;max-height:100%;"></video>`;
}

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
  const precio = pr ? pr.precio : (p.precio != null && Number(p.precio) > 0 ? Number(p.precio) : null);
  if(precio == null) return `<span class="prod-price" style="color:var(--azul-claro);">Consultar</span>`;
  const sz = large ? 'font-size:2.2rem' : 'font-size:1.4rem';
  let oferta = p.precioOferta != null ? Number(p.precioOferta) : null;
  if (!oferta && p.descuento && p.descuento > 0) oferta = Math.round(precio * (1 - p.descuento / 100));
  if (oferta && oferta < precio) {
    return `<div class="prod-price-block">
      <span class="prod-price-original">S/ ${precio.toLocaleString('es-PE')}</span>
      <span class="prod-price-now" style="${sz}">S/ ${oferta.toLocaleString('es-PE')}</span>
    </div>`;
  }
  return `<div class="prod-price-block">
    <span class="prod-price-now" style="${sz}">S/ ${precio.toLocaleString('es-PE')}</span>
  </div>`;
}
function getCarPrice(p) {
  const pr = PRICES[p.id];
  const precio = pr ? pr.precio : (p.precio != null && Number(p.precio) > 0 ? Number(p.precio) : null);
  if (!precio) return `<span class="car-price-consultar">Consultar</span>`;
  let oferta = p.precioOferta != null ? Number(p.precioOferta) : null;
  if (!oferta && p.descuento && p.descuento > 0) oferta = Math.round(precio * (1 - p.descuento / 100));
  if (oferta && oferta < precio) {
    return `<span class="car-price-original">S/ ${precio.toLocaleString('es-PE')}</span>
            <span class="car-price-now">S/ ${oferta.toLocaleString('es-PE')}</span>`;
  }
  return `<span class="car-price-now">S/ ${precio.toLocaleString('es-PE')}</span>`;
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
const CART_KEY = 'ft_cart';
function saveCart() { localStorage.setItem(CART_KEY, JSON.stringify(cart)); }
function loadCart() {
  try { cart = JSON.parse(localStorage.getItem(CART_KEY) || '[]'); } catch(e) { cart = []; }
}
let cart = [];
loadCart();
let activeFilter = 'all';
let visibleCount = 9;
let currentSlide = 0;
let slideInterval;

// ═══════════════════════════ TICKER ═══════════════════════════
function buildTicker() {
  const track = document.getElementById('tickerTrack');
  if (!track) return;
  const items = CATS.map(c => c.name).concat(['Fabricación a Medida','Acero Galvanizado','Lonas PVC','Lima y Provincias']);
  const doubled = [...items,...items];
  track.innerHTML = doubled.map(i =>
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
const totalSlides = document.querySelectorAll('.hero-slide').length;
function nextSlide() { goSlide((currentSlide+1) % totalSlides); }
function prevSlide() { goSlide((currentSlide+totalSlides-1) % totalSlides); }
function startAutoSlide() {
  slideInterval = setInterval(nextSlide, 5000);
}
document.querySelectorAll('.hero-prev,.hero-next,.hero-dot').forEach(el => {
  el.addEventListener('click', () => { clearInterval(slideInterval); startAutoSlide(); });
});

// ═══════════════════════════ COLOR PALETTE ═══════════════════════════
const PALETTE = [
  {bg:'#1949c9', light:'#dce8ff'},
  {bg:'#0d7f5f', light:'#d0f4e8'},
  {bg:'#c0392b', light:'#fde8e7'},
  {bg:'#7c3aed', light:'#ede9fe'},
  {bg:'#d97706', light:'#fef3c7'},
  {bg:'#0891b2', light:'#e0f7fa'},
  {bg:'#059669', light:'#d1fae5'},
  {bg:'#be185d', light:'#fce7f3'},
];
const _CAT_IDX = {toldos:0, carpas:1, banderas:2, inflables:3, juegos:4, modulos:5, terminados:6, lonas:7};
function getCatColor(catId) {
  if (_CAT_IDX[catId] !== undefined) return PALETTE[_CAT_IDX[catId]];
  return PALETTE[Math.abs(parseInt(catId) || 0) % PALETTE.length];
}

// ═══════════════════════════ CAT SLIDER ═══════════════════════════
var _catSliderX = 0;
var _catOrigWidth = 0;
var _catBusy = false;

function catSliderNav(dir) {
  if (window.innerWidth <= 768) return;
  if (_catBusy) return;
  var track = document.getElementById('catGrid');
  if (!track || !_catOrigWidth) return;
  var step = (200 + 16) * 4;
  var newX = _catSliderX - dir * step;

  // Ir hacia atrás desde el inicio → salto instantáneo al final, sin animación
  if (dir < 0 && newX > 0) {
    _catSliderX = newX - _catOrigWidth;
    track.style.transition = 'none';
    track.style.transform = 'translateX(' + _catSliderX + 'px)';
    return;
  }

  _catSliderX = newX;
  _catBusy = true;
  track.style.transition = 'transform 0.38s cubic-bezier(0.4,0,0.2,1)';
  track.style.transform = 'translateX(' + _catSliderX + 'px)';

  // Al llegar a la zona de clones → salto silencioso de vuelta al inicio
  setTimeout(function() {
    _catBusy = false;
    if (_catSliderX <= -_catOrigWidth) {
      _catSliderX += _catOrigWidth;
      track.style.transition = 'none';
      track.style.transform = 'translateX(' + _catSliderX + 'px)';
    }
  }, 420);
}

function initCatInfinite() {
  if (window.innerWidth <= 768) return;
  requestAnimationFrame(function() {
    var track = document.getElementById('catGrid');
    if (!track || !track.children.length) return;
    _catOrigWidth = track.scrollWidth + 28;
    Array.from(track.children).forEach(function(item) {
      track.appendChild(item.cloneNode(true));
    });
  });
}

function initCatSliderDrag() {
  var track    = document.getElementById('catGrid');
  var viewport = track && track.parentElement;
  if (!track || !viewport) return;
  // En móvil usa scroll nativo, no drag JS
  if (window.innerWidth <= 768) return;
  var dragStartX = 0, dragStartSliderX = 0, isDragging = false, didDrag = false;

  viewport.addEventListener('mousedown', function(e) {
    isDragging = true; didDrag = false;
    dragStartX = e.clientX;
    dragStartSliderX = _catSliderX;
    viewport.style.cursor = 'grabbing';
    track.style.transition = 'none';
    e.preventDefault();
  });

  window.addEventListener('mousemove', function(e) {
    if (!isDragging) return;
    var delta = e.clientX - dragStartX;
    if (Math.abs(delta) > 4) didDrag = true;
    var maxScroll = _catOrigWidth * 2 - viewport.clientWidth;
    _catSliderX = Math.min(0, Math.max(-maxScroll, dragStartSliderX + delta));
    track.style.transform = 'translateX(' + _catSliderX + 'px)';
  });

  window.addEventListener('mouseup', function() {
    if (!isDragging) return;
    isDragging = false;
    viewport.style.cursor = '';
  });

  track.addEventListener('click', function(e) {
    if (didDrag) { e.stopPropagation(); didDrag = false; }
  }, true);
}

// ═══════════════════════════ CATEGORIES ═══════════════════════════
function buildCats() {
  // Calcular precio mínimo > 0 por categoría
  var minPriceByCat = {};
  (PRODUCTS || []).forEach(function(p) {
    if (p.precio && p.precio > 0) {
      if (!minPriceByCat[p.cat] || p.precio < minPriceByCat[p.cat]) {
        minPriceByCat[p.cat] = p.precio;
      }
    }
  });

  const catGrid = document.getElementById('catGrid');
  if (!catGrid) return;
  catGrid.innerHTML = CATS.map(c => {
    var min = minPriceByCat[c.id];
    var priceHtml = min
      ? 'Desde <strong>S/ ' + Number(min).toLocaleString('es-PE', {minimumFractionDigits:2, maximumFractionDigits:2}) + '</strong>'
      : 'Consultar precio';
    var imgHtml = c.img
      ? `<img src="${c.img}" alt="${c.name}" class="sbc-img" loading="lazy"
           onerror="this.style.display='none';this.nextElementSibling.style.display='flex'">`
      : '';
    return `
    <div class="sbc-card" onclick="openCatPage('${c.id}')">
      <div class="sbc-img-area">
        ${imgHtml}
        <div class="sbc-img-icon" style="display:${c.img ? 'none' : 'flex'}">
          <i class="bi ${c.icon}"></i>
        </div>
      </div>
      <div class="sbc-card-body">
        <div class="sbc-cat-name">${c.name} <i class="bi bi-chevron-right"></i></div>
        <div class="sbc-cat-price">${priceHtml}</div>
      </div>
    </div>`;
  }).join('');
}

// ═══════════════════════════ FILTER TABS ═══════════════════════════
function buildFilterTabs() { /* tabs eliminados */ }
function filterProducts(catId) {
  activeFilter = catId; visibleCount = PRODUCTS.length; renderProducts();
}

// ═══════════════════════════ PRODUCTS ═══════════════════════════
function getGradient(cat) {
  return getCatColor(cat).light;
}

function getBadgesHTML(p) {
  let b = '';
  if (p.stock === 0)                        b += '<span class="prod-badge badge-agotado">AGOTADO</span>';
  if (p.esNuevo)                            b += '<span class="prod-badge badge-new">NUEVO</span>';
  if (p.descuento && p.descuento > 0)       b += `<span class="prod-badge badge-sale">-${p.descuento}%</span>`;
  if (p.esDestacado)                        b += '<span class="prod-badge badge-hot">POPULAR</span>';
  return b;
}

function getCountdownHTML(p, idSuffix) {
  if (!p.fechaFinOferta || !p.descuento || p.descuento <= 0) return '';
  const end = new Date(p.fechaFinOferta);
  if (isNaN(end) || end <= new Date()) return '';
  return `<div class="oferta-countdown" id="cd-${idSuffix}">
    <i class="bi bi-clock-fill"></i> Oferta termina en:
    <span class="cd-time" data-end="${end.getTime()}">--:--:--</span>
  </div>`;
}

function tickCountdowns() {
  document.querySelectorAll('.cd-time').forEach(el => {
    const end = Number(el.dataset.end);
    const diff = end - Date.now();
    if (diff <= 0) { el.closest('.oferta-countdown').style.display = 'none'; return; }
    const h = Math.floor(diff / 3600000);
    const m = Math.floor((diff % 3600000) / 60000);
    const s = Math.floor((diff % 60000) / 1000);
    el.textContent = `${String(h).padStart(2,'0')}:${String(m).padStart(2,'0')}:${String(s).padStart(2,'0')}`;
  });
}
setInterval(tickCountdowns, 1000);

// ═══ IMG ARROWS (multi-image nav on product cards) ═══
function initProdImgCycle(container) {
  (container || document).querySelectorAll('.prod-img-area[data-imgs]').forEach(function(area) {
    var imgs = JSON.parse(area.getAttribute('data-imgs') || '[]');
    if (imgs.length <= 1) return;
    if (area.querySelector('.prod-img-arrow-prev')) return; // ya inicializado

    area.setAttribute('data-img-idx', '0');

    var prevBtn = document.createElement('button');
    prevBtn.className = 'prod-img-arrow prod-img-arrow-prev';
    prevBtn.innerHTML = '<i class="bi bi-chevron-left"></i>';
    prevBtn.addEventListener('click', function(e) { e.stopPropagation(); prodImgNav(area, -1); });

    var nextBtn = document.createElement('button');
    nextBtn.className = 'prod-img-arrow prod-img-arrow-next';
    nextBtn.innerHTML = '<i class="bi bi-chevron-right"></i>';
    nextBtn.addEventListener('click', function(e) { e.stopPropagation(); prodImgNav(area, 1); });

    area.appendChild(prevBtn);
    area.appendChild(nextBtn);

    var dotsWrap = document.createElement('div');
    dotsWrap.className = 'prod-img-dots';
    imgs.forEach(function(_, i) {
      var dot = document.createElement('div');
      dot.className = 'prod-img-dot' + (i === 0 ? ' active' : '');
      dotsWrap.appendChild(dot);
    });
    area.appendChild(dotsWrap);
  });
}

function prodImgNav(area, dir) {
  var imgs = JSON.parse(area.getAttribute('data-imgs') || '[]');
  if (imgs.length <= 1) return;
  var imgEl = area.querySelector('.prod-img-real');
  if (!imgEl) return;
  var idx = (parseInt(area.getAttribute('data-img-idx') || '0') + dir + imgs.length) % imgs.length;
  area.setAttribute('data-img-idx', idx);
  imgEl.src = imgs[idx];
  area.querySelectorAll('.prod-img-dot').forEach(function(dot, i) {
    dot.classList.toggle('active', i === idx);
  });
}
function renderProducts() {
  const grid = document.getElementById('productsGrid');
  if(!grid) return;
  grid.innerHTML = PRODUCTS.map(p => `
    <div class="prod-card" onclick="openModal(${p.id})">
      <div class="prod-img-area" style="background:${getGradient(p.cat)}" data-pid="${p.id}" data-imgs='${JSON.stringify(p.imgs||[])}'>
        ${p.imgs && p.imgs[0] ? `<img src="${p.imgs[0]}" alt="${p.name}" class="prod-img-real" loading="lazy"
          onerror="this.style.display='none';this.parentElement.querySelector('.prod-img-placeholder').style.display='flex'">
          <div class="prod-img-placeholder" style="display:none;position:absolute;inset:0"><i class="bi ${p.icon}" style="font-size:3rem;color:rgba(0,0,0,0.2)"></i></div>` :
          `<div class="prod-img-placeholder"><i class="bi ${p.icon}" style="font-size:3rem;color:rgba(0,0,0,0.2)"></i></div>`}
        <div class="prod-badge-wrap">${getBadgesHTML(p)}</div>
        <button class="prod-eye-btn" onclick="event.stopPropagation();openProductPage(${p.id})" title="Ver producto completo"><i class="bi bi-eye"></i></button>
        ${p.stock === 0
          ? `<button class="prod-quick-add prod-quick-agotado" disabled><i class="bi bi-slash-circle"></i> Agotado</button>`
          : `<button class="prod-quick-add" onclick="addCart(event,${p.id})"><i class="bi bi-bag-plus-fill"></i> +agregar carrito</button>`}
      </div>
      <div class="prod-body">
        <div class="prod-cat-tag">${p.catName}${p.subcatName ? ' › ' + p.subcatName : ''}</div>
        <div class="prod-name">${p.name}</div>
        ${getStarsHTML(p)}
        <div class="prod-desc">${p.desc.substring(0,95)}...</div>
        ${getCountdownHTML(p, p.id)}
        <div class="prod-footer">
          <div>${getPriceHTML(p)}<div class="prod-price-note">desde med. base</div></div>
          ${getWspCardBtn(p)}
        </div>
      </div>
    </div>
  `).join('');
  initProdImgCycle(grid);
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

function toSlug(str) {
  return str.toLowerCase()
    .normalize('NFD').replace(/[̀-ͯ]/g, '')
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-|-$/g, '');
}
function findProductBySlug(slug) {
  return PRODUCTS.find(p => toSlug(p.name) === slug);
}

function openProductPage(id) {
  const p = PRODUCTS.find(x => x.id === id);
  window.location.href = p ? '/producto/' + toSlug(p.name) : '/';
}

/** Pinta el contenido de la página de producto (producto.html) para el producto ya resuelto por el servidor. */
function renderProductPage(p) {
  if (!p) return;
  currentModalProduct = p;
  selectedMedida = p.medidas[0] || '';
  selectedColor = p.colores[0] || '';
  currentImgIdx = 0;
  renderProdPage(p);
  setTimeout(initProductZoom, 80);
  renderTambienInteresa('prodTambienSection', p.cat, [p.id]);
  addVistoRecientemente(p.id);
  renderVistoRecientemente(p.id);
  window.scrollTo({top:0, behavior:'smooth'});
  initProdTopbarSticky();
}

function closeProdPage() {
  cleanupProductZoom();
  const p = currentModalProduct;
  const cat = p ? CATS.find(c => c.id === p.cat) : null;
  window.location.href = cat ? '/categoria/' + toSlug(cat.name) : '/';
}

function switchProdTab(tab, btn) {
  document.querySelectorAll('.prod-tab-btn').forEach(b => b.classList.remove('active'));
  document.querySelectorAll('.prod-tab-panel').forEach(p => p.style.display = 'none');
  btn.classList.add('active');
  const map = { desc:'prodTabDesc', details:'prodTabDetails', videos:'prodTabVideos' };
  const el = document.getElementById(map[tab]);
  if (el) el.style.display = 'block';
}

function renderProdPage(p) {
  // Breadcrumb
  document.getElementById('prodBreadcrumb').innerHTML =
    `<a href="#" onclick="closeProdPage();return false;">Inicio</a> / ` +
    `<a href="#" onclick="openCatPage('${p.cat}');return false;">${p.catName}</a>` +
    (p.subcatName ? ` / ${p.subcatName}` : '') +
    ` / <strong>${p.name.toUpperCase()}</strong>`;

  // Nav arrows
  const sameCat = PRODUCTS.filter(x => x.cat === p.cat);
  const idx = sameCat.findIndex(x => x.id === p.id);
  const prevBtn = document.getElementById('prodNavPrevBtn');
  const nextBtn = document.getElementById('prodNavNextBtn');
  if(prevBtn) prevBtn.disabled = false;
  if(nextBtn) nextBtn.disabled = false;

  // Thumbnails + main image
  const thumbStrip = document.getElementById('prodThumbStrip');
  const mainImg = document.getElementById('prodMainImg');
  _pthumbOffset = 0;
  if(thumbStrip) thumbStrip.style.transform = '';
  if(p.imgs && p.imgs.length) {
    thumbStrip.innerHTML = p.imgs.map((img,i) => `
      <img src="${img}" alt="${p.name} ${i+1}" class="pthumb ${i===0?'active':''}"
        onclick="changeProdImg(${i})" loading="lazy"
        onerror="this.style.display='none'">`).join('');
    mainImg.src = p.imgs[0];
    mainImg.alt = p.name;
    updateThumbArrows(p.imgs.length);
  } else {
    thumbStrip.innerHTML = '';
    mainImg.src = `https://via.placeholder.com/600x460/e3f2fd/1565c0?text=${encodeURIComponent(p.name)}`;
    updateThumbArrows(0);
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
    <div class="prod-page-cat-tag">${p.catName}${p.subcatName ? ' › ' + p.subcatName : ''}</div>
    <div class="prod-page-name-row">
      <div class="prod-page-name">${p.name}</div>
      <button class="btn-share-prod" onclick="shareProduct()" title="Compartir"><i class="bi bi-share-fill"></i></button>
    </div>
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
    ${getCountdownHTML(p, 'page-' + p.id)}
    <div class="prod-page-actions">
      <div class="prod-page-row1">
        <div class="pp-qty-wrap">
          <button class="pp-qty-btn" onclick="ppQtyChange(-1)">-</button>
          <span class="pp-qty-num" id="ppQtyNum">1</span>
          <button class="pp-qty-btn" onclick="ppQtyChange(1)">+</button>
        </div>
        ${p.stock === 0
          ? `<button class="btn-cart-page btn-cart-agotado" disabled>AGOTADO</button>`
          : `<button class="btn-cart-page" onclick="addCartById(${p.id});showToast('${p.name} agregado al carrito')">AÑADIR AL CARRITO</button>`}
      </div>
      <button class="btn-wsp-page" onclick="cotizarWspPage()">
        <i class="bi bi-whatsapp"></i> COMPRAR EN WHATSAPP
      </button>
    </div>
    <div class="prod-price-excl">*</div>
    <div class="csb-prod">
      <div class="csb-header">
        <span class="csb-title">Pago y seguridad</span>
        <i class="bi bi-lock-fill csb-lock"></i>
      </div>
      <div class="csb-logos">
        <span class="csb-logo csb-visa">VISA</span>
        <span class="csb-logo csb-mc"><i class="bi bi-circle-fill" style="color:#eb001b;font-size:0.85rem;"></i><i class="bi bi-circle-fill" style="color:#f79e1b;font-size:0.85rem;margin-left:-6px;"></i></span>
        <span class="csb-logo csb-bbva">BBVA</span>
        <span class="csb-logo csb-bcp">BCP</span>
        <span class="csb-logo csb-yape">Yape</span>
      </div>
      <p class="csb-note">Su información de pago se procesa de forma segura. No almacenamos los datos de su tarjeta de crédito ni tenemos acceso a ella.</p>
      <div class="csb-policies">
        <div class="csb-policy">
          <i class="bi bi-shield-lock"></i>
          <span>POLÍTICA DE<br>SEGURIDAD</span>
        </div>
        <div class="csb-policy">
          <i class="bi bi-truck"></i>
          <span>POLÍTICA DE<br>ENTREGA</span>
        </div>
        <div class="csb-policy">
          <i class="bi bi-arrow-return-left"></i>
          <span>POLÍTICA DE<br>DEVOLUCIÓN</span>
        </div>
      </div>
    </div>
  `;

  // ── Tabs: Descripción / Detalles / Videos ──
  const hasVideos = p.videos && p.videos.length > 0;
  const tabsEl = document.getElementById('prodTabsSection');
  if (tabsEl) {
    tabsEl.innerHTML = `
      <div class="prod-tabs">
        <div class="prod-tabs-nav">
          <button class="prod-tab-btn active" onclick="switchProdTab('desc',this)">
            <i class="bi bi-file-text"></i> Descripción
          </button>
          <button class="prod-tab-btn" onclick="switchProdTab('details',this)">
            <i class="bi bi-list-check"></i> Detalles del producto
          </button>
          ${hasVideos ? `<button class="prod-tab-btn" onclick="switchProdTab('videos',this)">
            <i class="bi bi-play-circle"></i> Videos
          </button>` : ''}
        </div>

        <div class="prod-tab-panel" id="prodTabDesc">
          <div class="prod-tab-text">${p.descLarga || p.desc || 'Sin descripción disponible.'}</div>
        </div>

        <div class="prod-tab-panel" id="prodTabDetails" style="display:none;">
          ${p.features && p.features.length ? `
            <div class="prod-tab-sub">Características principales</div>
            <ul class="prod-page-features">
              ${p.features.map(f=>`<li><i class="bi bi-check-circle-fill"></i>${f}</li>`).join('')}
            </ul>` : ''}
          ${p.medidas && p.medidas.length ? `
            <div class="prod-tab-sub" style="margin-top:20px;">Medidas disponibles</div>
            <div style="display:flex;flex-wrap:wrap;gap:8px;margin-top:10px;">
              ${p.medidas.map(m=>`<span class="medida-tag-detail">${m}</span>`).join('')}
            </div>` : ''}
          ${p.unidad ? `
            <div style="margin-top:20px;font-size:0.9rem;color:#555;">
              <strong>Unidad de medida:</strong> ${p.unidad}
            </div>` : ''}
        </div>

        ${hasVideos ? `
        <div class="prod-tab-panel" id="prodTabVideos" style="display:none;">
          <div class="prod-tab-videos-grid">
            ${p.videos.map(v=>`<div class="prod-tab-video-wrap">${buildVideoPlayer(v,{})}</div>`).join('')}
          </div>
        </div>` : ''}
      </div>
    `;
  }

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
          <div class="prod-related-scroll" id="prodRelatedScroll">
            ${related.map(rp => `
              <div class="prod-card" onclick="openProductPage(${rp.id})">
                <div class="prod-img-area" style="background:${getGradient(rp.cat)}">
                  ${rp.imgs && rp.imgs[0]
                    ? `<img src="${rp.imgs[0]}" alt="${rp.name}" class="prod-img-real" loading="lazy"
                        onerror="this.style.display='none';this.parentElement.querySelector('.prod-img-placeholder').style.display='flex'">
                       <div class="prod-img-placeholder" style="display:none;position:absolute;inset:0"><i class="bi ${rp.icon}" style="font-size:3rem;color:rgba(21,101,192,0.35)"></i></div>`
                    : `<div class="prod-img-placeholder"><i class="bi ${rp.icon}" style="font-size:3rem;color:rgba(21,101,192,0.35)"></i></div>`}
                  <div class="prod-badge-wrap">${getBadgesHTML(rp)}</div>
                  <div class="prod-actions-hover">
                    ${rp.stock === 0
                      ? `<div class="act-btn act-btn-agotado" title="Agotado"><i class="bi bi-slash-circle"></i></div>`
                      : `<div class="act-btn" onclick="addCart(event,${rp.id})" title="Agregar al carrito"><i class="bi bi-bag-plus"></i></div>`}
                  </div>
                </div>
                <div class="prod-body">
                  <div class="prod-cat-tag">${rp.catName}${rp.subcatName ? ' › ' + rp.subcatName : ''}</div>
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
      requestAnimationFrame(() => initDragScroll('prodRelatedScroll'));
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

function ppQtyChange(d) {
  const el = document.getElementById('ppQtyNum');
  if (!el) return;
  let v = parseInt(el.textContent) + d;
  if (v < 1) v = 1;
  el.textContent = v;
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
  const prevIdx = idx <= 0 ? sameCat.length - 1 : idx - 1;
  if(sameCat[prevIdx]) openProductPage(sameCat[prevIdx].id);
}

function prodNavNext() {
  if(!currentModalProduct) return;
  const sameCat = PRODUCTS.filter(x => x.cat === currentModalProduct.cat);
  const idx = sameCat.findIndex(x => x.id === currentModalProduct.id);
  const nextIdx = idx >= sameCat.length - 1 ? 0 : idx + 1;
  if(sameCat[nextIdx]) openProductPage(sameCat[nextIdx].id);
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
      ${p.stock === 0
        ? `<button class="btn-add-cart-modal btn-cart-agotado" disabled><i class="bi bi-slash-circle"></i> Agotado</button>`
        : `<button class="btn-add-cart-modal" onclick="addCartModal(${p.id})"><i class="bi bi-bag-plus-fill"></i> Agregar al carrito de consulta</button>`}
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
  if(p.stock === 0) { showToast('Este producto está agotado'); return; }
  const ex = cart.find(i=>i.id===id);
  if(ex) ex.qty++;
  else cart.push({...p, qty:1});
  saveCart();
  updateCartCount();
  showToast(`${p.name} agregado al carrito`);
}
function updateCartCount() {
  const total = cart.reduce((s,i)=>s+i.qty,0);
  document.getElementById('cartCount').textContent = total;
  const el = document.getElementById('cartHeadCount');
  if(el) el.textContent = `${total} producto${total!==1?'s':''}`;
  const lbl = document.getElementById('cartTotalLabel');
  if(lbl) {
    const sum = cart.reduce((s,i)=>{
      const pr = PRICES[i.id];
      const p = pr ? pr.precio : (i.precio != null && Number(i.precio) > 0 ? Number(i.precio) : 0);
      const o = i.precioOferta != null ? Number(i.precioOferta) : null;
      return s + (o && o < p ? o : p) * i.qty;
    },0);
    lbl.textContent = 'S/ ' + sum.toLocaleString('es-PE',{minimumFractionDigits:2,maximumFractionDigits:2});
  }
}

/* ═══ MODAL COTIZACIÓN ═══ */
function openCotizModal() {
  document.getElementById('cotizOverlay').classList.add('open');
  document.body.style.overflow = 'hidden';
}
function closeCotizModal(e) {
  if (e && e.target !== document.getElementById('cotizOverlay')) return;
  document.getElementById('cotizOverlay').classList.remove('open');
  document.body.style.overflow = '';
}
document.addEventListener('keydown', function(e) {
  if (e.key === 'Escape') {
    closeCotizModal();
    const overlay = document.getElementById('wpopupOverlay');
    if (overlay && overlay.classList.contains('open')) {
      overlay.classList.remove('open');
      localStorage.setItem('ft_wpopup_dismissed', Date.now());
    }
  }
});

async function submitCotiz(e) {
  e.preventDefault();
  const btn = document.getElementById('cotizSubmitBtn');
  ftSetBtnLoading(btn, 'Enviando...');

  const form = document.getElementById('cotizForm');
  const data = new URLSearchParams(new FormData(form));

  const csrfToken  = document.querySelector('meta[name="_csrf"]')?.content || '';
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

  try {
    const res = await fetch('/cotizar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded', [csrfHeader]: csrfToken },
      body: data
    });
    const json = await res.json();
    if (json.status === 'ok') {
      showToast('¡Solicitud enviada! Abriendo WhatsApp...');
      closeCotizModal();
      form.reset();
      setTimeout(() => window.open(json.wspUrl, '_blank'), 600);
    } else {
      showToast('Error al enviar. Intente por WhatsApp.');
    }
  } catch {
    showToast('Error de conexión. Intente por WhatsApp.');
  }
  ftRestoreBtn(btn);
}

function toggleMiCuentaMenu(e) {
  e.stopPropagation();
  const wrap = e.currentTarget.closest('.micuenta-wrap');
  const isOpen = wrap.classList.contains('open');
  document.querySelectorAll('.micuenta-wrap.open').forEach(w=>w.classList.remove('open'));
  if(!isOpen) wrap.classList.add('open');
}
document.addEventListener('click', function(){
  document.querySelectorAll('.micuenta-wrap.open').forEach(w=>w.classList.remove('open'));
});
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
  const fmt = n => Number(n).toLocaleString('es-PE', {minimumFractionDigits:2, maximumFractionDigits:2});
  body.innerHTML = cart.map(item => {
    const pr = PRICES[item.id];
    const precio = pr ? pr.precio : (item.precio != null && Number(item.precio) > 0 ? Number(item.precio) : null);
    const subtotalItem = precio ? `${item.qty} × S/${fmt(precio)}` : `${item.qty} unid.`;
    const img = item.imgs && item.imgs[0]
      ? `<img src="${item.imgs[0]}" alt="${item.name}" onerror="this.style.display='none'">`
      : `<i class="bi ${item.icon}" style="font-size:1.5rem;color:var(--azul-claro)"></i>`;
    return `
    <div class="cart-item">
      <div class="cart-item-thumb">${img}</div>
      <div class="cart-item-info">
        <div class="cart-item-nm">${item.name.toUpperCase()}</div>
        <div class="cart-item-price">${subtotalItem}</div>
      </div>
      <div class="cart-item-actions">
        <div class="cart-qty-ctrl">
          <button class="cart-qty-btn" onclick="changeQty(${item.id},-1)"><i class="bi bi-dash"></i></button>
          <span class="cart-qty-num">${item.qty}</span>
          <button class="cart-qty-btn" onclick="changeQty(${item.id},1)"><i class="bi bi-plus"></i></button>
        </div>
        <button class="cart-del" onclick="removeItem(${item.id})"><i class="bi bi-x-lg"></i></button>
      </div>
    </div>`;
  }).join('');

  const subtotal = cart.reduce((sum, item) => {
    const pr = PRICES[item.id];
    const precio = pr ? pr.precio : (item.precio != null && Number(item.precio) > 0 ? Number(item.precio) : 0);
    return sum + (Number(precio) * item.qty);
  }, 0);
  const subtotalStr = subtotal > 0 ? `S/${fmt(subtotal)}` : '—';

  foot.innerHTML = `
    <div class="cart-subtotal-row">
      <span class="cart-subtotal-lbl">Subtotal:</span>
      <span class="cart-subtotal-val">${subtotalStr}</span>
    </div>
    <button class="btn-ver-carrito" onclick="window.location.href='/cliente/carrito'">VER CARRITO</button>
    <button class="btn-finalizar-compra" onclick="window.location.href='/checkout'">
      FINALIZAR COMPRA
    </button>
  `;
}
function changeQty(id, d) {
  const item = cart.find(i=>i.id===id);
  if(!item) return;
  item.qty += d;
  if(item.qty <= 0) cart = cart.filter(i=>i.id!==id);
  saveCart(); updateCartCount(); renderCart();
}
function removeItem(id) { cart = cart.filter(i=>i.id!==id); saveCart(); updateCartCount(); renderCart(); }
function clearCart() { cart = []; saveCart(); updateCartCount(); renderCart(); }
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
  const prodEl = document.getElementById('footerProd');
  if (prodEl) prodEl.innerHTML = CATS.slice(0,5).map(c=>`
    <li><a href="#productos" onclick="filterProducts('${c.id}')"><i class="bi bi-chevron-right"></i>${c.name}</a></li>`).join('');
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

function hideAllViews() {
  ['mainView','catPageView','prodPageView','galPageView']
    .forEach(id => { const el = document.getElementById(id); if(el) el.style.display = 'none'; });
}

function goHome() {
  const main = document.getElementById('mainView');
  if (!main) { window.location.href = '/'; return; }
  closeAllDropdowns();
  closeMobileMenu && closeMobileMenu();
  hideAllViews();
  main.style.display = 'block';
  history.pushState({type:'inicio'}, '', '/');
  window.scrollTo({top: 0, behavior: 'smooth'});
}

function handlePromoClick(accion) {
  if (!accion) return;
  if (accion.startsWith('http') || accion.startsWith('/')) {
    window.open(accion, '_blank');
  } else {
    openCatPage(accion);
  }
}

function openCatPage(catId) {
  const cat = CATS.find(c => c.id === catId);
  const slug = cat ? toSlug(cat.name) : toSlug(catId || 'all');
  window.location.href = '/categoria/' + slug;
}

/** Pinta el contenido de la página de categoría (categoria.html) para la categoría ya resuelta por el servidor. */
function renderCategoryPage(catId, opts) {
  opts = opts || {};
  cpCat = catId || 'all';
  cpSearch = opts.search || '';
  cpPerPage = 9; cpSort = 'default'; cpGridCols = 3;
  cpBadges = []; cpColors = [];
  cpMedida = opts.medida || '';
  const si = document.getElementById('catSearchInput');
  if(si) si.value = cpSearch;
  buildSidebarCats();
  renderCatPage();
  window.scrollTo({top:0, behavior:'smooth'});
}

function closeCatPage() {
  window.location.href = '/';
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
        <div class="prod-img-area" style="background:${getGradient(p.cat)}" data-pid="${p.id}" data-imgs='${JSON.stringify(p.imgs||[])}'>
          ${p.imgs && p.imgs[0]
            ? `<img src="${p.imgs[0]}" alt="${p.name}" class="prod-img-real" loading="lazy"
                onerror="this.style.display='none';this.parentElement.querySelector('.prod-img-placeholder').style.display='flex'">
               <div class="prod-img-placeholder" style="display:none;position:absolute;inset:0"><i class="bi ${p.icon}" style="font-size:3rem;color:rgba(21,101,192,0.35)"></i></div>`
            : `<div class="prod-img-placeholder"><i class="bi ${p.icon}" style="font-size:3rem;color:rgba(21,101,192,0.35)"></i></div>`}
          <div class="prod-badge-wrap">${getBadgesHTML(p)}</div>
          <button class="prod-eye-btn" onclick="event.stopPropagation();openProductPage(${p.id})" title="Ver producto completo"><i class="bi bi-eye"></i></button>
          ${p.stock === 0
            ? `<button class="prod-quick-add prod-quick-agotado" disabled><i class="bi bi-slash-circle"></i> Agotado</button>`
            : `<button class="prod-quick-add" onclick="addCart(event,${p.id})"><i class="bi bi-bag-plus-fill"></i> + Agregar Carrito</button>`}
        </div>
        <div class="prod-body">
          <div class="prod-cat-tag">${p.catName}${p.subcatName ? ' › ' + p.subcatName : ''}</div>
          <div class="prod-name">${p.name}</div>
          ${getStarsHTML(p)}
          <div class="prod-desc">${p.desc.substring(0,95)}...</div>
          <div class="prod-footer">
            <div>
              ${getPriceHTML(p)}
              <div class="prod-price-note">desde med. base</div>
            </div>
            ${getWspCardBtn(p)}
          </div>
        </div>
      </div>
    `).join('');
    initProdImgCycle(grid);
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

  const medidaCount = {};
  PRODUCTS.forEach(p => {
    (p.medidas || []).forEach(m => {
      const key = m.trim();
      if(key) medidaCount[key] = (medidaCount[key] || 0) + 1;
    });
  });

  const sorted = Object.entries(medidaCount)
    .sort((a, b) => b[1] - a[1] || a[0].localeCompare(b[0]))
    .slice(0, 12);

  // Pre-compute 2 product images per medida
  const medidaImgs = {};
  sorted.forEach(([m]) => {
    medidaImgs[m] = PRODUCTS.filter(p => (p.medidas || []).includes(m) && p.imgs && p.imgs[0]).slice(0, 2);
  });

  function buildMedImgs(prods) {
    if (!prods.length) return `<p style="color:#aaa;font-size:0.83rem;grid-column:1/-1;">Sin productos para esta medida</p>`;
    return prods.map(p => `
      <div class="nav-cat2-item" onclick="closeAllDropdowns();openProductPage(${p.id})">
        <div class="nav-cat2-circle">
          <img src="${p.imgs[0]}" alt="${p.name}" loading="lazy">
        </div>
        <div class="nav-cat2-name">${p.name}</div>
      </div>`).join('');
  }

  const btns = sorted.map(([m, cnt]) => `
    <div class="medida-nav-btn" data-medida="${m}" onclick="openCatByMedida('${m}');closeAllDropdowns()">
      <span class="mnb-size">${m}</span>
      <span class="mnb-count">${cnt} prod${cnt !== 1 ? 's' : ''}</span>
    </div>
  `).join('');

  const defaultProds = sorted.length > 0 ? (medidaImgs[sorted[0][0]] || []) : [];

  menu.innerHTML = `
    <div class="nav-medida-inner">
      <div class="nav-medida-left">
        <span class="nav-mega-section-title" style="padding:0 0 2px 0;display:block">
          <i class="bi bi-rulers" style="margin-right:6px;color:var(--azul-claro)"></i>Buscar por Medida
        </span>
        <div class="medida-nav-grid">${btns}</div>
        <div class="nav-drop-divider"></div>
        <div class="medida-cta" onclick="closeAllDropdowns();openCatPage('all')">
          <i class="bi bi-pencil-square" style="color:var(--azul-claro)"></i>
          ¿Otra medida? <strong>Ver catálogo completo →</strong>
        </div>
      </div>
      <div class="nav-medida-photos nav-cat2-circles" id="navMedidaPhotosArea">
        ${buildMedImgs(defaultProds)}
      </div>
    </div>
  `;

  // Hover: swap photos per medida
  menu.querySelectorAll('.medida-nav-btn').forEach(btn => {
    btn.addEventListener('mouseenter', () => {
      const area = document.getElementById('navMedidaPhotosArea');
      if (!area) return;
      const prods = medidaImgs[btn.dataset.medida] || [];
      area.style.opacity = '0';
      setTimeout(() => {
        area.innerHTML = buildMedImgs(prods);
        area.style.opacity = '1';
      }, 120);
    });
  });
}

function openCatByMedida(medida) {
  window.location.href = '/categoria/all?medida=' + encodeURIComponent(medida);
}

// ═══════════════════════════ NAV DROPDOWNS ═══════════════════════════
function buildNavDropdowns() {
  const catMenu = document.getElementById('navCatMenu');
  if(catMenu) {
    catMenu.classList.add('nav-cat2-menu');

    function buildCat2Right(catId, catName) {
      const prods = catId === 'all'
        ? PRODUCTS.filter(p => p.imgs && p.imgs[0]).slice(0, 10)
        : PRODUCTS.filter(p => String(p.cat) === String(catId) && p.imgs && p.imgs[0]).slice(0, 10);
      const circles = prods.map(p => `
        <div class="nav-cat2-item" onclick="closeAllDropdowns();openProductPage(${p.id})">
          <div class="nav-cat2-circle">
            <img src="${p.imgs[0]}" alt="${p.name}" loading="lazy">
            ${p.esDestacado ? '<span class="nav-cat2-hot">HOT</span>' : (p.esNuevo ? '<span class="nav-cat2-hot nav-cat2-new">NEW</span>' : '')}
          </div>
          <div class="nav-cat2-name">${p.name}</div>
        </div>`).join('');
      const link = catId === 'all' ? `openCatPage('all')` : `openCatPage('${catId}')`;
      return `
        <div class="nav-cat2-right-head" onclick="closeAllDropdowns();${link}">
          Todos ${catName} <i class="bi bi-chevron-right"></i>
        </div>
        <div class="nav-cat2-circles">
          ${circles || '<p style="color:#aaa;padding:24px 0;font-size:0.85rem;">Sin productos aún</p>'}
        </div>`;
    }

    const firstCat = CATS[0];
    const catListHtml = CATS.map((c, i) => `
      <div class="nav-cat2-left-item ${i===0?'active':''}" data-cat="${c.id}" data-catname="${c.name}"
           onmouseenter="navCat2Hover(this)" onclick="closeAllDropdowns();openCatPage('${c.id}')">
        <span>${c.name}</span><i class="bi bi-chevron-right"></i>
      </div>`).join('');

    catMenu.innerHTML = `
      <div class="nav-cat2-inner">
        <div class="nav-cat2-left">
          <div class="nav-cat2-left-item nav-cat2-todos" data-cat="all" data-catname="productos"
               onmouseenter="navCat2Hover(this)" onclick="closeAllDropdowns();openCatPage('all')">
            <span><i class="bi bi-grid-fill" style="margin-right:6px;color:var(--azul-claro)"></i>Todos</span>
            <i class="bi bi-chevron-right"></i>
          </div>
          ${catListHtml}
        </div>
        <div class="nav-cat2-right" id="navCat2Right">
          ${firstCat ? buildCat2Right(firstCat.id, firstCat.name) : ''}
        </div>
      </div>`;

    window._buildCat2Right = buildCat2Right;
  }

  const prodMenu = document.getElementById('navProdMenu');
  if(prodMenu) {
    prodMenu.classList.add('nav-mega-menu');

    const prodLinks = PRODUCTS.slice(0, 8).map(p => `
      <a href="#" class="nav-drop-item" onclick="event.preventDefault();closeAllDropdowns();openProductPage(${p.id})">
        <span>${p.name}</span>
      </a>
    `).join('');

    const carouselCards = PRODUCTS.map(p => {
      const wspMsg = encodeURIComponent(`Hola Full Toldos! Me interesa: *${p.name}*. Quisiera una cotización.`);
      return `
      <div class="nav-car-card" onclick="closeAllDropdowns();openProductPage(${p.id})">
        <div class="nav-car-img-wrap" style="background:${getGradient(p.cat)}">
          ${p.imgs && p.imgs[0]
            ? `<img src="${p.imgs[0]}" alt="${p.name}" class="nav-car-img" loading="lazy"
                onerror="this.parentElement.innerHTML='<div class=nav-car-img-ph><i class=bi\\ bi-box></i></div>'">`
            : `<div class="nav-car-img-ph"><i class="bi bi-box"></i></div>`}
          ${p.badge === 'new'  ? '<span class="nav-car-badge badge-new">Nuevo</span>' : ''}
          ${p.badge === 'hot'  ? '<span class="nav-car-badge badge-hot">Popular</span>' : ''}
          ${p.badge === 'sale' ? '<span class="nav-car-badge badge-sale">Oferta</span>' : ''}
        </div>
        <div class="nav-car-body">
          <div class="nav-car-name">${p.name}</div>
          <div class="nav-car-cat">${p.catName}</div>
          <div class="nav-car-price">${getCarPrice(p)}</div>
          <a class="nav-car-wsp-btn" href="https://wa.me/51999999999?text=${wspMsg}" target="_blank" onclick="event.stopPropagation()" title="Pedir por WhatsApp">
            <i class="bi bi-whatsapp"></i>
          </a>
        </div>
      </div>
    `}).join('');

    prodMenu.innerHTML = `
      <div class="nav-car-full">
        <div class="nav-car-wrap">
          <button class="nav-car-btn nav-car-prev" onclick="navCarMove(-1)"><i class="bi bi-chevron-left"></i></button>
          <div class="nav-car-viewport">
            <div class="nav-car-track" id="navCarTrack">${carouselCards}</div>
          </div>
          <button class="nav-car-btn nav-car-next" onclick="navCarMove(1)"><i class="bi bi-chevron-right"></i></button>
        </div>
        <div class="nav-car-footer">
          <a href="#" class="nav-drop-all" onclick="event.preventDefault();closeAllDropdowns();openCatPage('all')">
            Ver todos los productos <i class="bi bi-arrow-right"></i>
          </a>
        </div>
      </div>
    `;
    navCarIdx = 0;
    positionProdMenu();
  }

  // Hover toggle — el timer se cancela si el mouse entra al menú desplegable
  let closeTimer = null;

  document.querySelectorAll('.nav-dropdown').forEach(dropdown => {
    const menu = dropdown.querySelector('.nav-dropdown-menu');

    const openIt = () => {
      clearTimeout(closeTimer);
      closeAllDropdowns();
      dropdown.classList.add('open');
      if (dropdown.querySelector('#navCatMenu'))    positionCatMenu();
      if (dropdown.querySelector('#navProdMenu'))   positionProdMenu();
      if (dropdown.querySelector('#navMedidaMenu')) positionMedidaMenu();
    };
    const scheduleClose = () => {
      closeTimer = setTimeout(() => dropdown.classList.remove('open'), 250);
    };

    dropdown.addEventListener('mouseenter', openIt);
    dropdown.addEventListener('mouseleave', scheduleClose);

    if (menu) {
      menu.addEventListener('mouseenter', () => clearTimeout(closeTimer));
      menu.addEventListener('mouseleave', scheduleClose);
    }
  });

  document.querySelectorAll('.nav-dropdown-toggle').forEach(toggle => {
    toggle.addEventListener('click', e => {
      e.preventDefault();
      const goto = toggle.dataset.goto;
      if (goto === 'cat')    { closeAllDropdowns(); openCatPage('all'); return; }
      if (goto === 'medida') { closeAllDropdowns(); openCatPage('all'); return; }
      if (goto === 'sobre')  { window.location.href = '/sobre-nosotros'; return; }
      // Default: alternar dropdown
      const dropdown = toggle.closest('.nav-dropdown');
      if (!dropdown) return;
      const isOpen = dropdown.classList.contains('open');
      closeAllDropdowns();
      if (!isOpen) {
        dropdown.classList.add('open');
        if (dropdown.querySelector('#navCatMenu'))    positionCatMenu();
        if (dropdown.querySelector('#navProdMenu'))   positionProdMenu();
        if (dropdown.querySelector('#navMedidaMenu')) positionMedidaMenu();
      }
    });
  });

  // Cerrar dropdowns al hacer click fuera
  document.addEventListener('click', e => {
    if (!e.target.closest('.nav-dropdown')) closeAllDropdowns();
  });
}

function closeAllDropdowns() {
  document.querySelectorAll('.nav-dropdown.open').forEach(d => d.classList.remove('open'));
}

// ═══════════════════════════ VISTO RECIENTEMENTE ═══════════════════════════
const VISTO_KEY = 'ft_visto';
const VISTO_MAX = 8;

function addVistoRecientemente(id) {
  let list = JSON.parse(localStorage.getItem(VISTO_KEY) || '[]');
  list = list.filter(x => x !== id);
  list.unshift(id);
  if (list.length > VISTO_MAX) list = list.slice(0, VISTO_MAX);
  localStorage.setItem(VISTO_KEY, JSON.stringify(list));
}

function renderVistoRecientemente(currentId) {
  const sec = document.getElementById('prodVistoSection');
  if (!sec) return;
  const list = JSON.parse(localStorage.getItem(VISTO_KEY) || '[]')
    .filter(id => id !== currentId)
    .slice(0, 6);
  if (!list.length) { sec.innerHTML = ''; return; }

  const cards = list.map(id => {
    const p = PRODUCTS.find(x => x.id === id);
    if (!p) return '';
    return `
      <div class="visto-card" onclick="openProductPage(${p.id})">
        <div class="visto-img-wrap" style="background:${getGradient(p.cat)}">
          ${p.imgs && p.imgs[0]
            ? `<img src="${p.imgs[0]}" alt="${p.name}" class="visto-img" loading="lazy" onerror="this.style.display='none'">`
            : `<div class="visto-img-ph"><i class="bi ${p.icon}"></i></div>`}
        </div>
        <div class="visto-body">
          <div class="visto-cat">${p.catName}</div>
          <div class="visto-name">${p.name}</div>
          <div class="visto-price">${getCarPrice(p)}</div>
        </div>
      </div>`;
  }).join('');

  sec.innerHTML = `
    <div class="visto-section">
      <div class="visto-header">
        <i class="bi bi-clock-history"></i> Visto recientemente
      </div>
      <div class="visto-grid">${cards}</div>
    </div>`;
}

function initProdTopbarSticky() {
  // CSS position:sticky handles this — overflow-x:clip on body keeps it working
}

function navCat2Hover(el) {
  document.querySelectorAll('.nav-cat2-left-item.active').forEach(i => i.classList.remove('active'));
  el.classList.add('active');
  const right = document.getElementById('navCat2Right');
  if (!right || !window._buildCat2Right) return;
  right.style.opacity = '0';
  setTimeout(() => {
    right.innerHTML = window._buildCat2Right(el.dataset.cat, el.dataset.catname);
    right.style.opacity = '1';
  }, 90);
}

function positionCatMenu() {
  const catMenu = document.getElementById('navCatMenu');
  const navRow = document.querySelector('.navbar-nav-row .container');
  if (!catMenu || !navRow) return;
  const catDropdown = catMenu.closest('.nav-dropdown');
  if (!catDropdown) return;
  const rowRect = navRow.getBoundingClientRect();
  const dropRect = catDropdown.getBoundingClientRect();
  catMenu.style.setProperty('left', (rowRect.left - dropRect.left) + 'px', 'important');
  catMenu.style.setProperty('width', rowRect.width + 'px', 'important');
  catMenu.style.setProperty('min-width', 'unset', 'important');
}

function positionMedidaMenu() {
  const medidaMenu = document.getElementById('navMedidaMenu');
  const navRow = document.querySelector('.navbar-nav-row .container');
  if (!medidaMenu || !navRow) return;
  const medidaDropdown = medidaMenu.closest('.nav-dropdown');
  if (!medidaDropdown) return;
  const rowRect  = navRow.getBoundingClientRect();
  const dropRect = medidaDropdown.getBoundingClientRect();
  medidaMenu.style.setProperty('left',      (rowRect.left - dropRect.left) + 'px', 'important');
  medidaMenu.style.setProperty('width',     rowRect.width + 'px', 'important');
  medidaMenu.style.setProperty('min-width', 'unset', 'important');
}

function positionProdMenu() {
  const prodMenu = document.getElementById('navProdMenu');
  const navRow = document.querySelector('.navbar-nav-row .container');
  if (!prodMenu || !navRow) return;
  const prodDropdown = prodMenu.closest('.nav-dropdown');
  if (!prodDropdown) return;
  const rowRect = navRow.getBoundingClientRect();
  const dropRect = prodDropdown.getBoundingClientRect();
  const leftVal = (rowRect.left - dropRect.left) + 'px';
  const widthVal = rowRect.width + 'px';
  prodMenu.style.setProperty('left', leftVal, 'important');
  prodMenu.style.setProperty('width', widthVal, 'important');
  prodMenu.style.setProperty('min-width', 'unset', 'important');
}

let navCarIdx = 0;
const NAV_CAR_VISIBLE = 4;
function navCarMove(dir) {
  const track = document.getElementById('navCarTrack');
  if (!track) return;
  const cards = track.querySelectorAll('.nav-car-card');
  const max = Math.max(0, cards.length - NAV_CAR_VISIBLE);
  navCarIdx = Math.min(Math.max(navCarIdx + dir, 0), max);
  const cardW = cards[0] ? cards[0].offsetWidth + 14 : 0;
  track.style.transform = `translateX(-${navCarIdx * cardW}px)`;
}

// ═══════════════════════════ TABS PRODUCTOS ═══════════════════════════
let tprodActiveTab = 'all';
let tprodFeatIdx   = 0;
let tprodGridPage  = 0;
const TPROD_PER_PAGE = 6;

function getTprodProds() {
  if (tprodActiveTab === 'all') return [...PRODUCTS];
  return PRODUCTS.filter(p => p.badge === tprodActiveTab);
}

function tprodSwitchTab(tab) {
  tprodActiveTab = tab;
  tprodFeatIdx   = 0;
  tprodGridPage  = 0;
  document.querySelectorAll('.tprod-tab').forEach(b => b.classList.toggle('active', b.dataset.tab === tab));
  renderTprod();
}

function tprodFeatPrev() {
  const prods = getTprodProds();
  if (!prods.length) return;
  tprodFeatIdx = (tprodFeatIdx - 1 + prods.length) % prods.length;
  renderTprodFeat();
}

function tprodFeatNext() {
  const prods = getTprodProds();
  if (!prods.length) return;
  tprodFeatIdx = (tprodFeatIdx + 1) % prods.length;
  renderTprodFeat();
}

function tprodGridPrev() {
  if (tprodGridPage > 0) { tprodGridPage--; renderTprodGrid(); }
}

function tprodGridNext() {
  const prods = getTprodProds();
  if ((tprodGridPage + 1) * TPROD_PER_PAGE < prods.length) { tprodGridPage++; renderTprodGrid(); }
}

function renderTprodFeat() {
  const el = document.getElementById('tprodFeatured');
  if (!el) return;
  const prods = getTprodProds();
  if (!prods.length) { el.innerHTML = '<div style="padding:40px;text-align:center;color:#aaa;font-size:0.8rem;">Sin productos</div>'; return; }
  const p = prods[tprodFeatIdx % prods.length];
  const wspMsg = encodeURIComponent(`Hola Full Toldos! Me interesa: *${p.name}*. Quisiera una cotización.`);
  el.innerHTML = `
    <div class="tprod-feat-card" onclick="openProductPage(${p.id})">
      <div class="tprod-feat-img" style="background:${getGradient(p.cat)}">
        ${p.imgs && p.imgs[0]
          ? `<img src="${p.imgs[0]}" alt="${p.name}" loading="lazy" onerror="this.style.display='none'">`
          : `<i class="bi ${p.icon}" style="font-size:5rem;color:rgba(0,0,0,0.12)"></i>`}
        ${p.badge === 'new'  ? '<span class="tprod-badge badge-new">Nuevo</span>'   : ''}
        ${p.badge === 'hot'  ? '<span class="tprod-badge badge-hot">Popular</span>' : ''}
        ${p.badge === 'sale' ? '<span class="tprod-badge badge-sale">Oferta</span>' : ''}
      </div>
      <div class="tprod-feat-body">
        <div class="tprod-feat-name">${p.name}</div>
        <div class="tprod-feat-cat">${p.catName}</div>
        ${getPriceHTML(p)}
      </div>
    </div>`;
}

function renderTprodGrid() {
  const el = document.getElementById('tprodGrid');
  if (!el) return;
  const prods = getTprodProds();
  const start = tprodGridPage * TPROD_PER_PAGE;
  const page  = prods.slice(start, start + TPROD_PER_PAGE);
  if (!page.length) {
    el.innerHTML = '<div style="grid-column:1/-1;padding:40px;text-align:center;color:#aaa;font-size:0.8rem;">Sin productos en esta categoría</div>';
    return;
  }
  el.innerHTML = page.map(p => {
    const wspMsg = encodeURIComponent(`Hola Full Toldos! Me interesa: *${p.name}*. Quisiera una cotización.`);
    return `
    <div class="tprod-card" onclick="openProductPage(${p.id})">
      <div class="tprod-card-img" style="background:${getGradient(p.cat)}">
        ${p.imgs && p.imgs[0]
          ? `<img src="${p.imgs[0]}" alt="${p.name}" loading="lazy" onerror="this.style.display='none'">`
          : `<i class="bi ${p.icon}" style="font-size:2.5rem;color:rgba(0,0,0,0.15)"></i>`}
        ${p.badge === 'new'  ? '<span class="tprod-badge badge-new">Nuevo</span>'   : ''}
        ${p.badge === 'hot'  ? '<span class="tprod-badge badge-hot">Popular</span>' : ''}
        ${p.badge === 'sale' ? '<span class="tprod-badge badge-sale">Oferta</span>' : ''}
      </div>
      <div class="tprod-card-body">
        <div class="tprod-card-name">${p.name}</div>
        <div class="tprod-card-cat">${p.catName}</div>
        ${getPriceHTML(p)}
        <button class="btn-wsp-tprod" onclick="event.stopPropagation();window.open('https://wa.me/51999999999?text=${wspMsg}','_blank')">
          <i class="bi bi-whatsapp"></i> Pedido por WhatsApp
        </button>
      </div>
    </div>`;
  }).join('');
}

function renderTprod() {
  renderTprodFeat();
  renderTprodGrid();
}

// ═══════════════════════════ SCROLL TOP + HIDE NAV ═══════════════════════════
(function initScrollBehavior() {
  const tb = document.querySelector('.topbar-ft');
  const nb = document.querySelector('.navbar-ft');
  let lastY = window.scrollY;
  let tbH = 0, nbH = 0;

  function updateHeights() {
    tbH = tb ? tb.getBoundingClientRect().height : 0;
    nbH = nb ? nb.getBoundingClientRect().height : 0;
  }
  updateHeights();
  window.addEventListener('resize', updateHeights);

  function setStickyBars(navVisible) {
    const prodTopbar = document.querySelector('.prod-page-topbar');
    const catTopbar  = document.querySelector('#catPageView .cat-page-header');
    const offset = navVisible ? (tbH + nbH) + 'px' : '0px';
    if (prodTopbar) prodTopbar.style.top = offset;
    if (catTopbar)  catTopbar.style.top  = offset;
  }

  window.addEventListener('scroll', () => {
    const y = window.scrollY;

    document.getElementById('scrollTopBtn').classList.toggle('visible', y > 400);

    if (y > 80 && y > lastY) {
      if (tb) tb.style.transform = 'translateY(-100%)';
      if (nb) nb.style.transform = `translateY(${-(tbH + nbH)}px)`;
      setStickyBars(false);
    } else {
      if (tb) tb.style.transform = '';
      if (nb) nb.style.transform = '';
      setStickyBars(true);
    }

    lastY = y;
  }, { passive: true });
})();

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
  if (!document.getElementById('mainView')) { window.location.href = '/'; return; }
  galActiveCat = catId || 'all';
  prevGalView = 'main';
  buildGalleryItems();
  hideAllViews();
  document.getElementById('galPageView').style.display = 'block';
  renderGallery();
  window.scrollTo({top:0, behavior:'smooth'});
}

// ═══════════════════════════ THUMB STRIP NAV ═══════════════════════════
let _pthumbOffset = 0;
const THUMB_SIZE = 88; // 80px + 8px gap
const THUMB_VISIBLE = 4;

function pthumbScroll(dir) {
  const strip = document.getElementById('prodThumbStrip');
  if (!strip) return;
  const total = strip.querySelectorAll('.pthumb').length;
  const isMobile = window.innerWidth <= 991;
  const max = Math.max(0, total - THUMB_VISIBLE);
  _pthumbOffset = Math.max(0, Math.min(_pthumbOffset + dir, max));
  strip.style.transform = isMobile
    ? `translateX(-${_pthumbOffset * THUMB_SIZE}px)`
    : `translateY(-${_pthumbOffset * THUMB_SIZE}px)`;
  updateThumbArrows(total);
}

function updateThumbArrows(total) {
  const up   = document.getElementById('pthumbUp');
  const down = document.getElementById('pthumbDown');
  if (!up || !down) return;
  if (total > THUMB_VISIBLE) {
    up.classList.toggle('visible',   _pthumbOffset > 0);
    down.classList.toggle('visible', _pthumbOffset < total - THUMB_VISIBLE);
  } else {
    up.classList.remove('visible');
    down.classList.remove('visible');
  }
}

function ftSetBtnLoading(btn, textoCarga) {
  if (!btn) return;
  btn.dataset.ftOriginalHtml = btn.innerHTML;
  btn.disabled = true;
  btn.innerHTML = '<i class="bi bi-arrow-repeat ft-spin-icon"></i> ' + textoCarga;
}
function ftRestoreBtn(btn) {
  if (!btn) return;
  btn.disabled = false;
  if (btn.dataset.ftOriginalHtml) btn.innerHTML = btn.dataset.ftOriginalHtml;
}

async function submitContacto(e) {
  e.preventDefault();
  const form    = e.target;
  const btn     = form.querySelector('button[type="submit"]');
  const nombre  = form.nombre.value.trim();
  const email   = form.email.value.trim();
  const mensaje = form.mensaje.value.trim();

  ftSetBtnLoading(btn, 'Enviando...');

  const csrfToken  = document.querySelector('meta[name="_csrf"]')?.content || '';
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

  try {
    const res = await fetch('/contacto/enviar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded', [csrfHeader]: csrfToken },
      body: new URLSearchParams({ nombre, email, mensaje })
    });
    if (!res.ok) throw new Error('fallo el envio');
    showToast('¡Mensaje enviado! Te responderemos pronto.');
    form.reset();
  } catch (err) {
    showToast('No se pudo enviar el mensaje. Intenta nuevamente.');
  } finally {
    ftRestoreBtn(btn);
  }
}

async function submitNewsletter(e) {
  e.preventDefault();
  const form  = e.target;
  const btn   = form.querySelector('button[type="submit"]');
  const email = form.email.value.trim();

  ftSetBtnLoading(btn, 'Enviando...');

  const csrfToken  = document.querySelector('meta[name="_csrf"]')?.content || '';
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

  try {
    const res = await fetch('/newsletter/suscribir', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded', [csrfHeader]: csrfToken },
      body: new URLSearchParams({ email })
    });
    if (!res.ok) throw new Error('fallo el envio');
    showToast('¡Suscrito! Gracias.');
    form.reset();
  } catch (err) {
    showToast('No se pudo completar la suscripción.');
  } finally {
    ftRestoreBtn(btn);
  }
}

// ═══════════════════════════ SOBRE NOSOTROS ═══════════════════════════
let _sobreObserver = null;

function initSobreScroll() {
  const blocks  = document.querySelectorAll('.sobre-text-block');
  const mainImg = document.getElementById('sobreMainImg');
  if (!blocks.length || !mainImg) return;

  if (_sobreObserver) _sobreObserver.disconnect();
  _sobreObserver = new IntersectionObserver(entries => {
    entries.forEach(e => {
      if (e.isIntersecting) {
        const src = e.target.dataset.img;
        if (src && !mainImg.src.endsWith(src)) {
          mainImg.style.opacity = '0';
          setTimeout(() => { mainImg.src = src; mainImg.style.opacity = '1'; }, 220);
        }
      }
    });
  }, { threshold: 0.45, rootMargin: '-10% 0px -10% 0px' });
  blocks.forEach(b => _sobreObserver.observe(b));
}

function toggleSobreFaq(btn) {
  const item   = btn.closest('.sobre-faq-item');
  const isOpen = item.classList.contains('open');
  document.querySelectorAll('.sobre-faq-item.open').forEach(i => i.classList.remove('open'));
  if (!isOpen) item.classList.add('open');
}

function closeGallery() {
  hideAllViews();
  if(prevGalView === 'category')     document.getElementById('catPageView').style.display = 'block';
  else if(prevGalView === 'product') document.getElementById('prodPageView').style.display = 'block';
  else                               document.getElementById('mainView').style.display     = 'block';
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
let _lbMode      = 'gallery'; // 'gallery' | 'product'
let _prodLbItems = [];
let _prodLbIdx   = 0;

function openLightbox(idx) {
  _lbMode = 'gallery';
  lightboxIdx = idx;
  updateLightbox();
  document.getElementById('lightboxOverlay').classList.add('open');
  document.body.style.overflow = 'hidden';
}

function openProdImgLightbox(startIdx) {
  const p = currentModalProduct;
  if (!p || !p.imgs || !p.imgs.length) return;
  cleanupProductZoom();
  _lbMode      = 'product';
  _prodLbItems = p.imgs;
  _prodLbIdx   = startIdx || 0;
  _updateProdLb();
  document.getElementById('lightboxOverlay').classList.add('open');
  document.body.style.overflow = 'hidden';
}

function _updateProdLb() {
  const p    = currentModalProduct;
  const img  = document.getElementById('lightboxImg');
  const name = document.getElementById('lightboxName');
  const cat  = document.getElementById('lightboxCat');
  const vBtn = document.getElementById('lightboxViewBtn');
  const prev = document.querySelector('.lightbox-prev');
  const next = document.querySelector('.lightbox-next');
  img.style.opacity = '0';
  setTimeout(() => { img.src = _prodLbItems[_prodLbIdx]; img.style.opacity = '1'; }, 140);
  if (name) name.textContent = p ? p.name : '';
  if (cat)  cat.textContent  = (_prodLbIdx + 1) + ' / ' + _prodLbItems.length;
  if (vBtn) vBtn.style.display = 'none';
  if (prev) prev.disabled = _prodLbIdx <= 0;
  if (next) next.disabled = _prodLbIdx >= _prodLbItems.length - 1;
}

function closeLightbox() {
  document.getElementById('lightboxOverlay').classList.remove('open');
  document.body.style.overflow = '';
  if (_lbMode === 'product') {
    _lbMode = 'gallery';
    const vBtn = document.getElementById('lightboxViewBtn');
    if (vBtn) vBtn.style.display = '';
  }
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
  if (_lbMode === 'product') {
    const n = _prodLbIdx + dir;
    if (n < 0 || n >= _prodLbItems.length) return;
    _prodLbIdx = n;
    _updateProdLb();
    return;
  }
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
                <div class="prod-cat-tag">${p.catName}${p.subcatName ? ' › ' + p.subcatName : ''}</div>
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
  if(!el) return;
  const max = el.scrollWidth - el.clientWidth;
  if(dir > 0 && el.scrollLeft >= max - 5) {
    el.scrollTo({ left: 0, behavior: 'smooth' });
  } else if(dir < 0 && el.scrollLeft <= 5) {
    el.scrollTo({ left: max, behavior: 'smooth' });
  } else {
    el.scrollBy({ left: dir * 260, behavior: 'smooth' });
  }
}

// Drag con mouse para cualquier scroll horizontal
function initDragScroll(id) {
  const el = document.getElementById(id);
  if(!el) return;
  let startX = 0, startLeft = 0, dragging = false, moved = false;

  el.addEventListener('mousedown', function(e) {
    dragging = true; moved = false;
    startX = e.clientX;
    startLeft = el.scrollLeft;
    el.style.cursor = 'grabbing';
    el.style.userSelect = 'none';
    e.preventDefault();
  });

  window.addEventListener('mousemove', function(e) {
    if(!dragging) return;
    const delta = e.clientX - startX;
    if(Math.abs(delta) > 4) moved = true;
    el.scrollLeft = startLeft - delta;
  });

  window.addEventListener('mouseup', function() {
    if(!dragging) return;
    dragging = false;
    el.style.cursor = 'grab';
    el.style.userSelect = '';
  });

  el.addEventListener('click', function(e) {
    if(moved) { e.stopPropagation(); moved = false; }
  }, true);

  el.style.cursor = 'grab';
}

function initTambienAutoScroll(id) {
  const el = document.getElementById(id);
  if(!el) return;
  initDragScroll(id);
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

  let html = '';

  // 1. Inicio
  html += `<a href="#" class="mob-nav-link" onclick="event.preventDefault();closeMobileMenu();goHome()">
    <span><i class="bi bi-house-fill" style="margin-right:8px;color:var(--azul-claro)"></i>Inicio</span>
  </a>`;

  // 2. Categorías accordion
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

  // 3. Productos accordion
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

  // 4. Por Medida accordion
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

  // 5. Galería
  html += `<div class="mob-nav-link" onclick="closeMobileMenu();openGallery()">
    <span><i class="bi bi-images" style="margin-right:8px;color:var(--azul-claro)"></i>Galería</span>
  </div>`;

  // 5b. Envíos
  html += `<a href="/envios" class="mob-nav-link">
    <span><i class="bi bi-truck" style="margin-right:8px;color:var(--azul-claro)"></i>Envíos</span>
  </a>`;

  // 6. Sobre Nosotros accordion
  html += `<div class="mob-nav-link" onclick="mobToggleSub('mobSubSobre',this)">
    <span><i class="bi bi-building" style="margin-right:8px;color:var(--azul-claro)"></i>Sobre Nosotros</span>
    <i class="bi bi-chevron-right mob-arrow"></i>
  </div>
  <div class="mob-sub" id="mobSubSobre">
    <a class="mob-sub-link" href="/sobre-nosotros">
      <i class="bi bi-building"></i><span>About Us</span>
    </a>
    <a class="mob-sub-link" href="/sobre-nosotros#resenas">
      <i class="bi bi-star-fill"></i><span>Preguntas frecuentes</span>
    </a>
  </div>`;

  // 7. Contacto
  html += `<a href="/contacto" class="mob-nav-link">
    <span><i class="bi bi-envelope-fill" style="margin-right:8px;color:var(--azul-claro)"></i>Contacto</span>
  </a>`;

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
  window.location.href = '/categoria/all?search=' + encodeURIComponent(q);
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
  initNavSearchDropdown();
}

// ── Search dropdown ──────────────────────────────────────────────
let _searchTimer = null;
let _searchKbdIdx = -1;

function initNavSearchDropdown() {
  const input = document.getElementById('navSearchInput');
  if (!input) return;
  input.addEventListener('focus', () => { _searchKbdIdx = -1; showSearchDropdown(input.value); });
  input.addEventListener('input', () => {
    _searchKbdIdx = -1;
    clearTimeout(_searchTimer);
    _searchTimer = setTimeout(() => showSearchDropdown(input.value), 110);
  });
  document.addEventListener('mousedown', (e) => {
    const c = document.querySelector('.nav-search-container');
    if (c && !c.contains(e.target)) hideSearchDropdown();
  });
}

function handleNavSearchKey(e) {
  const d = document.getElementById('navSearchDropdown');
  const items = d ? [...d.querySelectorAll('.search-drop-item')] : [];
  if (e.key === 'ArrowDown') {
    e.preventDefault();
    _searchKbdIdx = Math.min(_searchKbdIdx + 1, items.length - 1);
    items.forEach((el, i) => el.classList.toggle('kbd-focus', i === _searchKbdIdx));
    if (items[_searchKbdIdx]) {
      const nameEl = items[_searchKbdIdx].querySelector('.search-drop-name');
      if (nameEl) document.getElementById('navSearchInput').value = nameEl.textContent;
    }
  } else if (e.key === 'ArrowUp') {
    e.preventDefault();
    _searchKbdIdx = Math.max(_searchKbdIdx - 1, -1);
    items.forEach((el, i) => el.classList.toggle('kbd-focus', i === _searchKbdIdx));
  } else if (e.key === 'Enter') {
    if (_searchKbdIdx >= 0 && items[_searchKbdIdx]) { items[_searchKbdIdx].click(); }
    else { doNavSearch(); hideSearchDropdown(); }
  } else if (e.key === 'Escape') {
    hideSearchDropdown();
  }
}

function hideSearchDropdown() {
  const d = document.getElementById('navSearchDropdown');
  if (d) { d.classList.remove('open'); d.innerHTML = ''; }
  _searchKbdIdx = -1;
}

function showSearchDropdown(rawQuery) {
  const d = document.getElementById('navSearchDropdown');
  if (!d) return;
  const query = (rawQuery || '').trim().toLowerCase();

  let results, sectionTitle, alsoItems;

  if (!query) {
    // Featured: hot/new first, fill to 6
    const featured = PRODUCTS.filter(p => p.badge === 'hot' || p.badge === 'new');
    const rest     = PRODUCTS.filter(p => !featured.includes(p));
    results        = [...featured, ...rest].slice(0, 6);
    sectionTitle   = 'Productos destacados';
    // Also: categories
    alsoItems = CATS.slice(0, 6).map(c => ({
      icon: c.icon, label: c.name, type: 'cat', id: c.id
    }));
  } else {
    results = PRODUCTS.filter(p =>
      p.name.toLowerCase().includes(query) ||
      (p.desc || '').toLowerCase().includes(query) ||
      (p.catName || '').toLowerCase().includes(query)
    ).slice(0, 6);
    sectionTitle = results.length
      ? 'Resultados para "' + rawQuery.trim() + '"'
      : null;
    // Also: products from categories not already in results
    const usedCats = new Set(results.map(p => p.cat));
    const alsoProd = PRODUCTS.filter(p => !results.includes(p) && !usedCats.has(p.cat)).slice(0, 4);
    alsoItems = alsoProd.map(p => ({ icon: p.icon, label: p.name, type: 'prod', id: p.id }));
    // Pad with categories
    if (alsoItems.length < 4) {
      CATS.filter(c => !usedCats.has(c.id)).slice(0, 4 - alsoItems.length).forEach(c => {
        alsoItems.push({ icon: c.icon, label: c.name, type: 'cat', id: c.id });
      });
    }
  }

  let html = '';

  if (results.length) {
    html += `<div class="search-drop-section">
      <span class="search-drop-section-title">${escHtml(sectionTitle)}</span>
      ${results.map(p => buildSearchItem(p, query)).join('')}
    </div>`;
    if (query) {
      html += `<div class="search-drop-all" onclick="doNavSearch();hideSearchDropdown();">
        <i class="bi bi-search"></i> Ver todos los resultados de &ldquo;${escHtml(rawQuery.trim())}&rdquo;
      </div>`;
    }
  } else if (query) {
    html += `<div class="search-no-results">
      <i class="bi bi-search"></i>
      Sin resultados para <strong>"${escHtml(rawQuery.trim())}"</strong>
    </div>`;
  }

  if (alsoItems.length) {
    const chips = alsoItems.map(it =>
      `<span class="search-also-chip" data-type="${it.type}" data-id="${it.id}"
             onclick="searchChipClick(this)">
         <i class="bi ${it.icon}"></i> ${escHtml(it.label)}
       </span>`
    ).join('');
    html += `<div class="search-also-section">
      <span class="search-also-title">Quizás también necesites</span>
      <div class="search-also-chips">${chips}</div>
    </div>`;
  }

  d.innerHTML = html;
  d.classList.add('open');
}

function buildSearchItem(p, query) {
  const imgHtml = (p.imgs && p.imgs[0])
    ? `<img src="${p.imgs[0]}" alt="${escHtml(p.name)}" loading="lazy">`
    : `<i class="bi ${p.icon}" style="font-size:1.25rem;color:rgba(255,255,255,0.85);"></i>`;
  const bg = (p.imgs && p.imgs[0]) ? '' : `style="background:${(p.colores||['#1949c9'])[0]};width:100%;height:100%;display:flex;align-items:center;justify-content:center;"`;
  const nameHl = query
    ? escHtml(p.name).replace(new RegExp('(' + escRe(query) + ')', 'gi'),
        '<span class="search-drop-hl">$1</span>')
    : escHtml(p.name);
  return `<div class="search-drop-item" onclick="openModal(${p.id});hideSearchDropdown();">
    <div class="search-drop-thumb"><div ${bg}>${imgHtml}</div></div>
    <div>
      <div class="search-drop-name">${nameHl}</div>
      <div class="search-drop-cat">${escHtml(p.catName || '')}</div>
    </div>
  </div>`;
}

function searchChipClick(el) {
  const type = el.dataset.type, id = el.dataset.id;
  hideSearchDropdown();
  if (type === 'cat') openCatPage(id);
  else if (type === 'prod') openModal(parseInt(id));
}

function escHtml(s) { return String(s).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c])); }
function escRe(s)   { return s.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'); }

function doNavSearch() {
  const q      = (document.getElementById('navSearchInput').value || '').trim();
  const catId  = document.getElementById('navSearchCat').value || 'all';
  const catObj = CATS.find(c => c.id === catId);
  const slug   = catObj ? toSlug(catObj.name) : 'all';
  closeAllDropdowns();
  window.location.href = '/categoria/' + slug + (q ? '?search=' + encodeURIComponent(q) : '');
}

// ═══════════════════════════ MERGE DB DATA ═══════════════════════════
// Los IDs de BD se suman a 100000 para evitar conflicto con los IDs hardcodeados (1-15)
(function mergeDbData() {
  if (typeof DB_CATS !== 'undefined' && Array.isArray(DB_CATS)) {
    DB_CATS.forEach(function(c) {
      // Las cats DB tienen id numérico-string ('1','2'...), las hardcoded tienen slug ('toldos'...)
      // No hay conflicto de id, se agregan directamente
      CATS.push(c);
    });
  }
  if (typeof DB_PRODUCTS !== 'undefined' && Array.isArray(DB_PRODUCTS)) {
    DB_PRODUCTS.forEach(function(p) {
      // Usar offset para evitar conflicto con IDs hardcodeados 1-15
      p.id = 100000 + Number(p.id);
      // El cat del producto ya es string del id de categoría DB ('1','2'...)
      PRODUCTS.push(p);
      // Actualizar contador en la categoría correspondiente
      var cat = CATS.find(function(c) { return c.id === p.cat; });
      if (cat) cat.count = (cat.count || 0) + 1;
    });
  }
})();

// ═══════════════════════════ INIT ═══════════════════════════
buildTicker();
buildCats();
initCatInfinite();
initCatSliderDrag();
buildFilterTabs();
renderProducts();

// Navegación por URL: abre producto o categoría según parámetros
(function urlRouting() {
  const path = window.location.pathname;
  const prodMatch = path.match(/^\/producto\/([^/]+)/);
  const catMatch  = path.match(/^\/categoria\/([^/]+)/);
  const params = new URLSearchParams(window.location.search);
  const prodId = prodMatch ? decodeURIComponent(prodMatch[1]) : params.get('producto');
  const catId  = catMatch  ? decodeURIComponent(catMatch[1])  : params.get('categoria');
  const catOpts = { search: params.get('search'), medida: params.get('medida') };
  if (prodId) {
    setTimeout(function() {
      const p = findProductBySlug(prodId);
      if (p) renderProductPage(p);
    }, 100);
  } else if (catId) {
    setTimeout(function() {
      if (catId === 'all') { renderCategoryPage('all', catOpts); return; }
      const cat = CATS.find(c => toSlug(c.name) === catId) || CATS.find(c => c.id === catId);
      if (cat) renderCategoryPage(cat.id, catOpts);
    }, 100);
  }
  window.addEventListener('popstate', function(e) {
    const st = e.state;
    if (!st) { closeProdPage(); return; }
    if (st.type === 'producto') renderProductPage(PRODUCTS.find(x => x.id === st.id));
    else if (st.type === 'categoria') renderCategoryPage(st.id);
    else closeProdPage();
  });
})();

renderTprod();
buildFooter();
buildNavDropdowns();
buildNavSearch();
buildMedidaDropdown();
buildMobileMenu();
startAutoSlide();
buildClientesGal();
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
    document.querySelectorAll('.hero-section, .env-hero').forEach(el => el.style.marginTop = totalH + 'px');
    document.querySelectorAll('#catPageView,#prodPageView,#galPageView,#sobrePageView,#contactoPageView').forEach(el => {
      if(el.style.display !== 'none') el.style.paddingTop = totalH + 'px';
    });
  };
  apply();
  window.addEventListener('resize', apply);
})();

// ═══ GALLERY FULL VIEW MODAL ═══
function openGalleryModal() {
  const p = currentModalProduct;
  if (!p) return;
  const hasVids = p.videos && p.videos.length > 0;
  switchGalleryTab(hasVids ? 'vids' : 'imgs');
  document.getElementById('galleryLikeBtn').classList.remove('liked');
  document.getElementById('galleryLikeCount').textContent = String(Math.floor(Math.random() * 80) + 10);
  document.getElementById('galleryModal').classList.add('active');
  document.body.style.overflow = 'hidden';
}

function closeGalleryModal(e) {
  if (e && e.target !== document.getElementById('galleryModal')) return;
  document.getElementById('galleryModal').classList.remove('active');
  const vid = document.querySelector('#galleryMainVid video');
  if (vid) vid.pause();
  document.body.style.overflow = '';
}

function switchGalleryTab(tab) {
  const p = currentModalProduct;
  if (!p) return;
  document.getElementById('tabImgs').classList.toggle('active', tab === 'imgs');
  document.getElementById('tabVids').classList.toggle('active', tab === 'vids');
  const mainImg = document.getElementById('galleryMainImg');
  const mainVid = document.getElementById('galleryMainVid');
  const panel   = document.getElementById('galleryThumbPanel');

  if (tab === 'imgs') {
    mainImg.style.display = 'block';
    mainVid.style.display = 'none';
    const imgs = p.imgs || [];
    if (imgs.length) {
      mainImg.src = imgs[0];
      mainImg.alt = p.name;
      panel.innerHTML =
        `<div class="gallery-section-title">Imágenes del producto</div>` +
        imgs.map((src, i) =>
          `<div class="gallery-list-item${i===0?' active':''}" onclick="selectGalleryImg(${i})">
            <div class="gallery-list-thumb">
              <img src="${src}" alt="${p.name} ${i+1}" loading="lazy" onerror="this.parentElement.style.background='#eee'">
            </div>
            <div class="gallery-list-info">
              <div class="gallery-list-title">${p.name} — Imagen ${i+1}</div>
              <div class="gallery-list-author">${p.catName || ''}</div>
            </div>
          </div>`
        ).join('');
      setGalleryMeta(p.name + ' — Imagen 1', p.catName || '');
    } else {
      mainImg.src = '';
      mainImg.style.display = 'none';
      panel.innerHTML = `<div class="gallery-no-content"><i class="bi bi-images"></i><span>Sin imágenes</span></div>`;
      setGalleryMeta('', '');
    }
  } else {
    mainImg.style.display = 'none';
    mainVid.style.display = 'flex';
    const vids = p.videos || [];
    if (vids.length) {
      mainVid.innerHTML = buildVideoPlayer(vids[0]);
      panel.innerHTML =
        `<div class="gallery-section-title">Videos del producto</div>` +
        vids.map((v, i) => {
          const isYt = !!getYouTubeId(v);
          return `<div class="gallery-list-item${i===0?' active':''}" onclick="selectGalleryVid(${i})">
            <div class="gallery-list-thumb" style="background:#1a1a2e;">
              ${isYt
                ? `<img src="https://img.youtube.com/vi/${getYouTubeId(v)}/mqdefault.jpg"
                       style="width:100%;height:100%;object-fit:cover;" onerror="this.style.display='none'">`
                : `<div class="gallery-list-play"><i class="bi bi-play-circle-fill"></i></div>`}
            </div>
            <div class="gallery-list-info">
              <div class="gallery-list-title">${p.name} — Video ${i+1}</div>
              <div class="gallery-list-author">${isYt ? 'YouTube' : 'Video local'}</div>
            </div>
          </div>`;
        }).join('');
      setGalleryMeta(p.name + ' — Video 1', p.catName || '');
    } else {
      mainVid.innerHTML = `<div class="gallery-no-content"><i class="bi bi-camera-video-off"></i><span>Sin videos disponibles</span></div>`;
      panel.innerHTML = `<div class="gallery-no-content"><i class="bi bi-camera-video-off"></i><span>Sin videos</span></div>`;
      setGalleryMeta('', '');
    }
  }
}

function setGalleryMeta(title, author) {
  document.getElementById('galleryMediaTitle').textContent = title;
  document.getElementById('galleryMediaAuthor').textContent = author;
}

function selectGalleryImg(idx) {
  const p = currentModalProduct;
  const imgs = (p && p.imgs) || [];
  const img = document.getElementById('galleryMainImg');
  if (img && imgs[idx]) {
    img.style.opacity = '0';
    setTimeout(() => { img.src = imgs[idx]; img.style.opacity = '1'; }, 140);
  }
  document.querySelectorAll('#galleryThumbPanel .gallery-list-item').forEach((el, i) =>
    el.classList.toggle('active', i === idx));
  if (p) setGalleryMeta(p.name + ' — Imagen ' + (idx + 1), p.catName || '');
}

function selectGalleryVid(idx) {
  const p = currentModalProduct;
  const vids = (p && p.videos) || [];
  const vid = document.getElementById('galleryMainVid');
  if (vid && vids[idx]) {
    vid.innerHTML = buildVideoPlayer(vids[idx], { autoplay: true });
  }
  document.querySelectorAll('#galleryThumbPanel .gallery-list-item').forEach((el, i) =>
    el.classList.toggle('active', i === idx));
  if (p) setGalleryMeta(p.name + ' — Video ' + (idx + 1), p.catName || '');
}

function galleryToggleLike(btn) {
  btn.classList.toggle('liked');
  const span = document.getElementById('galleryLikeCount');
  const n = parseInt(span.textContent) || 0;
  span.textContent = btn.classList.contains('liked') ? n + 1 : Math.max(0, n - 1);
}

document.addEventListener('keydown', function(e) {
  if (e.key === 'Escape') {
    const gm = document.getElementById('galleryModal');
    if (gm && gm.classList.contains('active')) closeGalleryModal();
  }
});

function shareProduct() {
  const p = currentModalProduct;
  if (!p) return;
  const title = p.name + ' — Full Toldos';
  const url = window.location.href;
  if (navigator.share) {
    navigator.share({ title, url }).catch(() => {});
  } else {
    navigator.clipboard.writeText(url).then(() => showToast('Enlace copiado al portapaperes'));
  }
}

/* ═══ GALERÍA — carrusel con imágenes reales ═══ */
function buildClientesGal() {
  const track = document.getElementById('clientesGalTrack');
  if (!track) return;

  // Recopilar todas las imágenes de los productos
  const imgs = [];
  PRODUCTS.forEach(p => {
    if (p.imgs && p.imgs.length) {
      p.imgs.forEach(src => imgs.push({ src, name: p.name }));
    } else if (p.imagenPrincipal) {
      imgs.push({ src: p.imagenPrincipal, name: p.name });
    }
  });

  if (!imgs.length) {
    // Si no hay imágenes de productos, mantener placeholders vacíos
    track.innerHTML = '<div class="clientes-gal-slide" style="background:#f0f4ff;display:flex;align-items:center;justify-content:center;border-radius:14px;"><i class="bi bi-images" style="font-size:3rem;color:#c5d4f5;"></i></div>'.repeat(2);
    return;
  }

  track.innerHTML = imgs.map(img => `
    <div class="clientes-gal-slide" onclick="openGallery()" style="cursor:pointer;">
      <img src="${img.src}" alt="${img.name}" loading="lazy"
           onerror="this.parentElement.style.display='none'">
    </div>`).join('');
}

(function() {
  var cgIdx = 0;
  var cgTotal = 0;
  var cgVisible = 2;

  function cgUpdate() {
    var track = document.getElementById('clientesGalTrack');
    if (!track) return;
    var slides = track.querySelectorAll('.clientes-gal-slide');
    cgTotal = slides.length;
    cgVisible = window.innerWidth <= 768 ? 1 : 2;
    var maxIdx = Math.max(0, cgTotal - cgVisible);
    if (cgIdx > maxIdx) cgIdx = maxIdx;
    var slideW = slides[0] ? slides[0].offsetWidth + 20 : 0;
    track.style.transform = 'translateX(-' + (cgIdx * slideW) + 'px)';
  }

  window.clientesGalNav = function(dir) {
    var track = document.getElementById('clientesGalTrack');
    if (!track) return;
    var slides = track.querySelectorAll('.clientes-gal-slide');
    cgTotal = slides.length;
    cgVisible = window.innerWidth <= 768 ? 1 : 2;
    var maxIdx = Math.max(0, cgTotal - cgVisible);
    cgIdx = Math.max(0, Math.min(cgIdx + dir, maxIdx));
    cgUpdate();
  };

  window.addEventListener('resize', cgUpdate);
  document.addEventListener('DOMContentLoaded', cgUpdate);
})();

// ═══════════════════════════ POPUP BIENVENIDA ═══════════════════════════
(function initWelcomePopup() {
  const STORAGE_KEY = 'ft_wpopup_dismissed';
  const DAYS = 7;

  function shouldShow() {
    const ts = localStorage.getItem(STORAGE_KEY);
    if (!ts) return true;
    return Date.now() - Number(ts) > DAYS * 86400000;
  }

  if (shouldShow()) {
    setTimeout(function() {
      const overlay = document.getElementById('wpopupOverlay');
      if (overlay) overlay.classList.add('open');
    }, 2500);
  }
})();

function closeWelcomePopup(e) {
  if (e && e.target !== document.getElementById('wpopupOverlay')) return;
  document.getElementById('wpopupOverlay').classList.remove('open');
  localStorage.setItem('ft_wpopup_dismissed', Date.now());
}

async function submitWelcomeEmail(e) {
  e.preventDefault();
  const form  = e.target;
  const btn   = form.querySelector('button[type="submit"]');
  const email = document.getElementById('wpopupEmail').value.trim();
  if (!email) return;

  ftSetBtnLoading(btn, 'Enviando...');

  const csrfToken  = document.querySelector('meta[name="_csrf"]')?.content || '';
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

  try {
    await fetch('/newsletter/suscribir', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded', [csrfHeader]: csrfToken },
      body: new URLSearchParams({ email })
    });
  } catch (err) {}

  ftRestoreBtn(btn);
  document.getElementById('wpopupOverlay').classList.remove('open');
  localStorage.setItem('ft_wpopup_dismissed', Date.now());
  showToast('¡Gracias! Te mantendremos informado.');
}

/* ═══════════════════════════════════════════════
   CAROUSEL: COMPRAR POR TAMAÑO
═══════════════════════════════════════════════ */
let sizeCarrIdx  = 0;
let sizeCarrData = [];
const SIZE_VISIBLE = 5;

function initSizeCarr() {
  const track = document.getElementById('sizeCarrTrack');
  const dots  = document.getElementById('sizeCarrDots');
  if (!track || !dots) return;

  // Construir mapa medida → primer producto con imagen
  const medidasMap = {};
  (DB_PRODUCTS || []).forEach(p => {
    if (!p.medidas || !Array.isArray(p.medidas)) return;
    const imgSrc = (p.imgs && p.imgs.length > 0) ? p.imgs[0] : '';
    p.medidas.forEach(m => {
      const key = (m || '').trim();
      if (key && !medidasMap[key]) {
        medidasMap[key] = imgSrc;
      }
    });
  });

  sizeCarrData = Object.entries(medidasMap).map(([label, img]) => ({ label, img }));

  if (sizeCarrData.length === 0) {
    const sec = document.querySelector('.size-section');
    if (sec) sec.style.display = 'none';
    return;
  }

  sizeCarrData.forEach((s, i) => {
    const card = document.createElement('div');
    card.className = 'size-carr-card';
    card.onclick   = () => openCatPage('', s.label);
    card.innerHTML = `
      <div class="size-carr-img">
        ${s.img
          ? `<img src="${s.img}" alt="${s.label}" loading="lazy">`
          : `<div style="width:100%;height:100%;display:flex;align-items:center;justify-content:center;color:#bbb;font-size:2.8rem;"><i class="bi bi-rulers"></i></div>`}
      </div>
      <div class="size-carr-label">${s.label}</div>`;
    track.appendChild(card);

    const dot = document.createElement('div');
    dot.className = 'size-carr-dot' + (i === 0 ? ' active' : '');
    dot.onclick = () => sizeCarrGoTo(i);
    dots.appendChild(dot);
  });

  sizeCarrUpdate();
}

function sizeCarrScroll(dir) {
  const max = Math.max(0, sizeCarrData.length - SIZE_VISIBLE);
  sizeCarrIdx = Math.max(0, Math.min(sizeCarrIdx + dir, max));
  sizeCarrUpdate();
}

function sizeCarrGoTo(idx) {
  const max = Math.max(0, sizeCarrData.length - SIZE_VISIBLE);
  sizeCarrIdx = Math.max(0, Math.min(idx, max));
  sizeCarrUpdate();
}

function sizeCarrUpdate() {
  const track   = document.getElementById('sizeCarrTrack');
  const prevBtn = document.getElementById('sizePrev');
  const nextBtn = document.getElementById('sizeNext');
  if (!track || !track.children.length) return;

  const cardW = track.children[0].offsetWidth + 16;
  track.style.transform = `translateX(-${sizeCarrIdx * cardW}px)`;

  document.querySelectorAll('.size-carr-dot').forEach((d, i) =>
    d.classList.toggle('active', i === sizeCarrIdx));
  if (prevBtn) prevBtn.disabled = sizeCarrIdx === 0;
  if (nextBtn) nextBtn.disabled = sizeCarrIdx >= sizeCarrData.length - SIZE_VISIBLE;
}

document.addEventListener('DOMContentLoaded', initSizeCarr);
