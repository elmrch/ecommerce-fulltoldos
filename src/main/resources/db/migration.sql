-- ================================================================
-- Tabla: configuracion (ajustes del sistema gestionados desde el admin)
-- ================================================================
CREATE TABLE IF NOT EXISTS public.configuracion (
    clave       VARCHAR(100) PRIMARY KEY,
    valor       TEXT,
    descripcion VARCHAR(255),
    grupo       VARCHAR(50)
);

-- ================================================================
-- Tabla: cotizaciones
-- ================================================================
CREATE TABLE IF NOT EXISTS public.cotizaciones (
    id               BIGSERIAL PRIMARY KEY,
    nombre           VARCHAR(100),
    apellido         VARCHAR(100),
    correo           VARCHAR(150),
    telefono         VARCHAR(30),
    empresa          VARCHAR(150),
    tipo_toldo       VARCHAR(100),
    medidas          VARCHAR(100),
    mensaje          TEXT,
    como_sentero     VARCHAR(200),
    fecha_solicitud  TIMESTAMP DEFAULT NOW(),
    estado           VARCHAR(20) DEFAULT 'PENDIENTE'
);

-- ================================================================
-- Migración: Subcategorías + Unidad de Medida
-- Ejecutar una sola vez en producción (Neon / PostgreSQL)
-- ================================================================

-- 1. Crear tabla subcategorias
CREATE TABLE IF NOT EXISTS public.subcategorias (
    id             BIGSERIAL PRIMARY KEY,
    categoria_id   BIGINT NOT NULL REFERENCES public.categorias(id),
    nombre         VARCHAR(150) NOT NULL,
    descripcion    TEXT,
    activo         BOOLEAN NOT NULL DEFAULT TRUE,
    orden          INTEGER NOT NULL DEFAULT 0
);

-- 2. Agregar columnas a productos
ALTER TABLE public.productos
    ADD COLUMN IF NOT EXISTS subcategoria_id BIGINT REFERENCES public.subcategorias(id),
    ADD COLUMN IF NOT EXISTS unidad_medida   VARCHAR(50),
    ADD COLUMN IF NOT EXISTS es_nuevo           BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS es_destacado       BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS descuento          INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS descripcion_larga  TEXT,
    ADD COLUMN IF NOT EXISTS fecha_fin_oferta   TIMESTAMP;

-- ================================================================
-- DATOS: Categorías
-- ================================================================
INSERT INTO public.categorias (nombre, descripcion, activo, orden) VALUES
    ('TOLDOS PUBLICITARIOS',    NULL, TRUE,  1),
    ('CARPAS ARMABLES',         NULL, TRUE,  2),
    ('BANDERAS PUBLICITARIAS',  NULL, TRUE,  3),
    ('INFLABLES PUBLICITARIOS', NULL, TRUE,  4),
    ('JUEGOS PUBLICITARIOS',    NULL, TRUE,  5),
    ('MODULOS PUBLICITARIOS',   NULL, TRUE,  6),
    ('PRODUCTOS TERMINADOS',    NULL, TRUE,  7),
    ('LONAS Y TEXTILES',        NULL, TRUE,  8),
    ('ESTRUCTURAS',             NULL, TRUE,  9),
    ('INSUMOS DE COSTURA',      NULL, TRUE, 10),
    ('ACCESORIOS',              NULL, TRUE, 11),
    ('EQUIPOS',                 NULL, TRUE, 12)
ON CONFLICT DO NOTHING;

-- ================================================================
-- DATOS: Subcategorías
-- (usa subconsultas para obtener el id de la categoría por nombre)
-- ================================================================
INSERT INTO public.subcategorias (categoria_id, nombre, activo, orden)
SELECT c.id, s.nombre, TRUE, s.orden
FROM (VALUES
    ('TOLDOS PUBLICITARIOS',    'TOLDOS PLEGABLES',              1),
    ('TOLDOS PUBLICITARIOS',    'TOLDOS ARMABLES',               2),
    ('TOLDOS PUBLICITARIOS',    'TOLDOS DE PARED',               3),
    ('TOLDOS PUBLICITARIOS',    'TOLDO DOMO',                    4),
    ('CARPAS ARMABLES',         'CARPAS A DOS AGUAS',            1),
    ('CARPAS ARMABLES',         'CARPAS TECHO PLANO',            2),
    ('BANDERAS PUBLICITARIAS',  'PLUMAS PUBLICITARIAS',          1),
    ('BANDERAS PUBLICITARIAS',  'GOTA PUBLICITARIA',             2),
    ('PRODUCTOS TERMINADOS',    'COBERTURAS DE TOLDOS',          1),
    ('PRODUCTOS TERMINADOS',    'COBERTURAS DE CARPAS',          2),
    ('PRODUCTOS TERMINADOS',    'EMBONES DE ACERO GALVANIZADO',  3),
    ('PRODUCTOS TERMINADOS',    'EMBONES DE ACERO ZINCADO',      4),
    ('PRODUCTOS TERMINADOS',    'EMBONRES DE ACERO LAC',         5),
    ('PRODUCTOS TERMINADOS',    'ESTRUCTURA COMPLETA TOLDO',     6),
    ('PRODUCTOS TERMINADOS',    'ESTRUCTURA COMPLETA CARPA',     7),
    ('PRODUCTOS TERMINADOS',    'REPUESTO TECHOS PLEGABLES',     8),
    ('PRODUCTOS TERMINADOS',    'REPUESTO PAREDES PLEGABLES',    9),
    ('LONAS Y TEXTILES',        'POLIETILENO ALTA DENSIDAD',     1),
    ('LONAS Y TEXTILES',        'LONA PVC 1 CARA',               2),
    ('LONAS Y TEXTILES',        'LONA PVC AMBAS CARAS',          3),
    ('LONAS Y TEXTILES',        'PLVC TRANSPARENTES',            4),
    ('LONAS Y TEXTILES',        'MALLAS PVC',                    5),
    ('ESTRUCTURAS',             'ESTRUCTURAS PARA TOLDO',        1),
    ('ESTRUCTURAS',             'ESTRUCTURAS PARA CARPAS',       2)
) AS s(cat_nombre, nombre, orden)
JOIN public.categorias c ON c.nombre = s.cat_nombre
ON CONFLICT DO NOTHING;

-- ================================================================
-- DATOS: Productos (LONAS Y TEXTILES)
-- ================================================================
INSERT INTO public.productos (categoria_id, subcategoria_id, nombre, unidad_medida, activo, precio, stock, fecha_creacion)
SELECT c.id, s.id, p.nombre, p.unidad, TRUE, 0, 0, CURRENT_DATE
FROM (VALUES
    ('LONAS Y TEXTILES', 'POLIETILENO ALTA DENSIDAD', 'TARFLEX 200 GR X 4MTS', 'METRO'),
    ('LONAS Y TEXTILES', 'POLIETILENO ALTA DENSIDAD', 'TARFLEX 200 GR X 2MTS', 'METRO'),
    ('LONAS Y TEXTILES', 'POLIETILENO ALTA DENSIDAD', 'TARFLEX 300 GR X 2MTS', 'METRO'),
    ('LONAS Y TEXTILES', 'LONA PVC 1 CARA',           'LINO PLASTIFICADO BOX',    'METRO'),
    ('LONAS Y TEXTILES', 'LONA PVC 1 CARA',           'LINO PLASTIFICADO PESADO', 'METRO'),
    ('LONAS Y TEXTILES', 'LONA PVC AMBAS CARAS',      'LONA TAIWAN',     'METRO'),
    ('LONAS Y TEXTILES', 'PLVC TRANSPARENTES',        'PVC PARA MICA',   'METRO'),
    ('LONAS Y TEXTILES', 'MALLAS PVC',                'MALLAS PVC',      'METRO')
) AS p(cat_nombre, sub_nombre, nombre, unidad)
JOIN public.categorias   c ON c.nombre = p.cat_nombre
JOIN public.subcategorias s ON s.nombre = p.sub_nombre AND s.categoria_id = c.id
ON CONFLICT DO NOTHING;

-- ================================================================
-- DATOS: Productos (ESTRUCTURAS PARA TOLDO)
-- ================================================================
INSERT INTO public.productos (categoria_id, subcategoria_id, nombre, unidad_medida, activo, precio, stock, fecha_creacion)
SELECT c.id, s.id, p.nombre, 'UNIDAD', TRUE, 0, 0, CURRENT_DATE
FROM (VALUES
    ('A DOS AGUAS 2X2'), ('A DOS AGUAS 2X3'), ('A DOS AGUAS 3X3'), ('A DOS AGUAS 3X4'),
    ('A DOS AGUAS 3X6'), ('A DOS AGUAS 4X4'), ('A DOS AGUAS 4X6'), ('A DOS AGUAS 5X5'),
    ('A DOS AGUAS VARIOS'),
    ('PIRAMIDAL 2X2'), ('PIRAMIDAL 2X3'), ('PIRAMIDAL 3X3'), ('PIRAMIDAL 3X4'),
    ('PIRAMIDAL 3X6'), ('PIRAMIDAL 4X4'), ('PIRAMIDAL 4X6'), ('PIRAMIDAL 5X5'),
    ('PIRAMIDAL VARIOS')
) AS p(nombre)
JOIN public.categorias    c ON c.nombre = 'ESTRUCTURAS'
JOIN public.subcategorias s ON s.nombre = 'ESTRUCTURAS PARA TOLDO' AND s.categoria_id = c.id
ON CONFLICT DO NOTHING;

-- ================================================================
-- DATOS: Productos (ESTRUCTURAS PARA CARPAS)
-- ================================================================
INSERT INTO public.productos (categoria_id, subcategoria_id, nombre, unidad_medida, activo, precio, stock, fecha_creacion)
SELECT c.id, s.id, p.nombre, 'UNIDAD', TRUE, 0, 0, CURRENT_DATE
FROM (VALUES
    ('A DOS AGUAS 2X2'), ('A DOS AGUAS 2X3'), ('A DOS AGUAS 3X3'), ('A DOS AGUAS 3X4'),
    ('A DOS AGUAS 3X6'), ('A DOS AGUAS 4X4'), ('A DOS AGUAS 4X6'), ('A DOS AGUAS 5X5'),
    ('PIRAMIDAL 3X3'), ('PIRAMIDAL 3X4'), ('PIRAMIDAL 3X6'),
    ('PIRAMIDAL 4X4'), ('PIRAMIDAL 4X6'), ('PIRAMIDAL 5X5'), ('PIRAMIDAL VARIOS')
) AS p(nombre)
JOIN public.categorias    c ON c.nombre = 'ESTRUCTURAS'
JOIN public.subcategorias s ON s.nombre = 'ESTRUCTURAS PARA CARPAS' AND s.categoria_id = c.id
ON CONFLICT DO NOTHING;

-- ================================================================
-- DATOS: Productos (INSUMOS DE COSTURA)
-- ================================================================
INSERT INTO public.productos (categoria_id, nombre, unidad_medida, activo, precio, stock, fecha_creacion)
SELECT c.id, p.nombre, p.unidad, TRUE, 0, 0, CURRENT_DATE
FROM (VALUES
    ('PEGA PEGA 2.5CM', 'METRO'), ('PEGA PEGA 5CM', 'METRO'),
    ('CINTA RIGIDA', 'METRO'),    ('CIERRE TRACTOR', 'METRO'),
    ('OJALILLOS', 'UNIDAD'),      ('HILO', 'UNIDAD'),
    ('TICTOC', 'UNIDAD')
) AS p(nombre, unidad)
JOIN public.categorias c ON c.nombre = 'INSUMOS DE COSTURA'
ON CONFLICT DO NOTHING;

-- ================================================================
-- DATOS: Productos (ACCESORIOS)
-- ================================================================
INSERT INTO public.productos (categoria_id, nombre, unidad_medida, activo, precio, stock, fecha_creacion)
SELECT c.id, p.nombre, p.unidad, TRUE, 0, 0, CURRENT_DATE
FROM (VALUES
    ('TEMPLADORES', 'UNIDAD'), ('CANDADOS', 'UNIDAD'), ('CABLE ACERADO', 'METRO')
) AS p(nombre, unidad)
JOIN public.categorias c ON c.nombre = 'ACCESORIOS'
ON CONFLICT DO NOTHING;

-- ================================================================
-- DATOS: Productos (EQUIPOS)
-- ================================================================
INSERT INTO public.productos (categoria_id, nombre, unidad_medida, activo, precio, stock, fecha_creacion)
SELECT c.id, p.nombre, 'UNIDAD', TRUE, 0, 0, CURRENT_DATE
FROM (VALUES
    ('SIN FIN PARA TOLDOS'), ('TROQUELADORA'), ('ACCESORIO DE TROQUEL')
) AS p(nombre)
JOIN public.categorias c ON c.nombre = 'EQUIPOS'
ON CONFLICT DO NOTHING;