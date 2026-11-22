package com.raizesdonordeste.api.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.raizesdonordeste.api.controller.dto.ProdutoDTO;
import com.raizesdonordeste.api.infrastructure.repository.ProdutoRepository;

@Service 
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoDTO::new)
                .collect(Collectors.toList());
    }
}
