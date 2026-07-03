package com.ecommerce.fulltoldos.repository;

import com.ecommerce.fulltoldos.model.Pedido;
import com.ecommerce.fulltoldos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByOrderByFechaCreacionDesc();
    List<Pedido> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
}