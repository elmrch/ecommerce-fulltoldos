package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.Producto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

/** Filtros reutilizables para la búsqueda paginada del catálogo público. */
public class ProductoSpecifications {

    private ProductoSpecifications() {}

    public static Specification<Producto> activo() {
        return (root, query, cb) -> cb.isTrue(root.get("activo"));
    }

    public static Specification<Producto> categoria(Long categoriaId) {
        return (root, query, cb) -> cb.equal(root.get("categoria").get("id"), categoriaId);
    }

    public static Specification<Producto> nombreContiene(String texto) {
        String like = "%" + texto.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("nombre")), like);
    }

    public static Specification<Producto> medidaContiene(String medida) {
        String like = "%" + medida + "%";
        return (root, query, cb) -> cb.like(root.get("medidas"), like);
    }

    public static Specification<Producto> colorEnAlguno(List<String> colores) {
        return (root, query, cb) -> {
            Predicate[] ors = colores.stream()
                    .map(c -> cb.like(root.get("colores"), "%" + c + "%"))
                    .toArray(Predicate[]::new);
            return cb.or(ors);
        };
    }

    /** Réplica de la prioridad de badge usada al pintar el producto (sale > hot > new). */
    public static Specification<Producto> badgeEnAlguno(List<String> badges) {
        return (root, query, cb) -> {
            Predicate sinDescuento = cb.or(
                    cb.isNull(root.get("descuento")),
                    cb.le(root.get("descuento"), 0));

            Predicate sale = cb.and(cb.isNotNull(root.get("descuento")), cb.gt(root.get("descuento"), 0));
            Predicate hot  = cb.and(sinDescuento, cb.isTrue(root.get("esDestacado")));
            Predicate nuevo = cb.and(sinDescuento, cb.isFalse(root.get("esDestacado")), cb.isTrue(root.get("esNuevo")));

            Predicate[] ors = badges.stream().map(b -> switch (b) {
                case "sale" -> sale;
                case "hot" -> hot;
                case "new" -> nuevo;
                default -> cb.disjunction();
            }).toArray(Predicate[]::new);

            return cb.or(ors);
        };
    }

    public static Specification<Producto> ofertaVigente() {
        return (root, query, cb) -> cb.and(
                cb.isNotNull(root.get("descuento")),
                cb.gt(root.get("descuento"), 0),
                cb.or(
                        cb.isNull(root.get("fechaFinOferta")),
                        cb.greaterThan(root.get("fechaFinOferta"), LocalDateTime.now())
                )
        );
    }
}
