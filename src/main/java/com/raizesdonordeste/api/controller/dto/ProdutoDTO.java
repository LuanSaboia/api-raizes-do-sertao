package com.raizesdonordeste.api.controller.dto;

import java.math.BigDecimal;

import com.raizesdonordeste.api.domain.model.Produto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class ProdutoDTO {
    
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private boolean sazonal;

    public ProdutoDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPrecoBase();
        this.sazonal = produto.getSazonal();
    }
    
}
