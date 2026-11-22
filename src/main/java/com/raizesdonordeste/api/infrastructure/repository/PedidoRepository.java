package com.raizesdonordeste.api.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raizesdonordeste.api.domain.enums.CanalPedido;
import com.raizesdonordeste.api.domain.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    
    List<Pedido> findByCanalPedido(CanalPedido canalPedido);
}
