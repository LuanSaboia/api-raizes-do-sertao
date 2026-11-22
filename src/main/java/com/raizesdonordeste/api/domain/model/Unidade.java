package com.raizesdonordeste.api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "unidades")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Unidade {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String nome;

    @Column (nullable = false)
    private String endereco;

    @Column (name = "possui_cozinha", nullable = false)
    private Boolean possuiCozinha = true;
}
