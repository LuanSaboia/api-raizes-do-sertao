package com.raizesdonordeste.api.domain.model;

import com.raizesdonordeste.api.domain.enums.CanalPedido;
import com.raizesdonordeste.api.domain.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity 
@Table (name = "pedidos")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Pedido {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn (name = "unidade_id", nullable = false)
    private Unidade unidade;
    
    @ManyToOne 
    @JoinColumn (name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Enumerated (EnumType.STRING)
    @JoinColumn (name = "canal_pedido", nullable = false)
    private CanalPedido canalPedido;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private StatusPedido status;

    @Column (name = "valor_total", nullable = false)
    private BigDecimal valorTotal;
    
    @Column (name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany (mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
}
