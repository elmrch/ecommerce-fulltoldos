-- ═══════════════════════════════════════════════════════════
--  MIGRACIÓN: Auditoría de tablas — Full Toldos
--  Ejecutar en psql o pgAdmin sobre la BD fulltoldos_bd
-- ═══════════════════════════════════════════════════════════

-- ── PRODUCTOS ──
-- fecha_creacion ya existe como DATE → convertir a TIMESTAMP
ALTER TABLE public.productos
  ALTER COLUMN fecha_creacion TYPE timestamp
  USING fecha_creacion::timestamp;

ALTER TABLE public.productos
  ADD COLUMN IF NOT EXISTS fecha_modificacion  timestamp,
  ADD COLUMN IF NOT EXISTS usuario_creacion    varchar(150),
  ADD COLUMN IF NOT EXISTS usuario_modificacion varchar(150);

-- Rellenar usuario_creacion para registros existentes
UPDATE public.productos
  SET usuario_creacion = 'sistema'
  WHERE usuario_creacion IS NULL;

-- ── CATEGORÍAS ──
ALTER TABLE public.categorias
  ADD COLUMN IF NOT EXISTS fecha_creacion       timestamp DEFAULT now(),
  ADD COLUMN IF NOT EXISTS fecha_modificacion   timestamp,
  ADD COLUMN IF NOT EXISTS usuario_creacion     varchar(150) DEFAULT 'sistema',
  ADD COLUMN IF NOT EXISTS usuario_modificacion varchar(150);

-- ── SUBCATEGORÍAS ──
ALTER TABLE public.subcategorias
  ADD COLUMN IF NOT EXISTS fecha_creacion       timestamp DEFAULT now(),
  ADD COLUMN IF NOT EXISTS fecha_modificacion   timestamp,
  ADD COLUMN IF NOT EXISTS usuario_creacion     varchar(150) DEFAULT 'sistema',
  ADD COLUMN IF NOT EXISTS usuario_modificacion varchar(150);

-- ── USUARIOS ──
ALTER TABLE public.usuarios
  ADD COLUMN IF NOT EXISTS fecha_creacion       timestamp DEFAULT now(),
  ADD COLUMN IF NOT EXISTS fecha_modificacion   timestamp,
  ADD COLUMN IF NOT EXISTS usuario_creacion     varchar(150) DEFAULT 'sistema',
  ADD COLUMN IF NOT EXISTS usuario_modificacion varchar(150);