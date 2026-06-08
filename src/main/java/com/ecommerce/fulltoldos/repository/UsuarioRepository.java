package com.ecommerce.fulltoldos.repository;


import com.ecommerce.fulltoldos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca usuario por email (lo usa Spring Security al hacer login)
    Optional<Usuario> findByEmail(String email);

    // Verifica si ya existe ese email (útil al registrar)
    boolean existsByEmail(String email);
}