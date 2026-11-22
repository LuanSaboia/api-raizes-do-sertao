package com.raizesdonordeste.api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "estoque_unidade")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class EstoqueUnidade {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn (name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column (nullable = false)
    private Integer quantidade;
}
