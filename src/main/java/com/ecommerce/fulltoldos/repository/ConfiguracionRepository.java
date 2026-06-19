package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, String> {
    List<Configuracion> findByGrupoOrderByClave(String grupo);
}
