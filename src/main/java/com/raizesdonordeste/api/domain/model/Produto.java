package com.raizesdonordeste.api.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "produtos")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Produto {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String nome;

    private String descricao;
    
    @Column (name = "preco_base", nullable = false)
    private BigDecimal precoBase;

    @Column (nullable = false)
    private Boolean sazonal = false;
}
