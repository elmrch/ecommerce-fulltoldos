package com.ecommerce.fulltoldos.service;


import com.ecommerce.fulltoldos.model.Rol;
import com.ecommerce.fulltoldos.model.Usuario;
import com.ecommerce.fulltoldos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ── Spring Security llama este método al hacer login ──
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.isActivo(),   // enabled — bloquea el login si está desactivado
                true,                 // accountNonExpired
                true,                 // credentialsNonExpired
                true,                 // accountNonLocked
                Collections.singletonList(
                        new SimpleGrantedAuthority(usuario.getRol().name())
                )
        );
    }

    // ── Registrar nuevo cliente ──
    public void registrarCliente(String nombre, String email, String password) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }
        Usuario nuevo = new Usuario(
                nombre,
                email,
                passwordEncoder.encode(password),
                Rol.ROLE_CLIENTE
        );
        usuarioRepository.save(nuevo);
    }

    // ── Registrar admin (solo desde código / datos iniciales) ──
    public void registrarAdmin(String nombre, String email, String password) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }
        Usuario admin = new Usuario(
                nombre,
                email,
                passwordEncoder.encode(password),
                Rol.ROLE_ADMIN
        );
        usuarioRepository.save(admin);
    }
}