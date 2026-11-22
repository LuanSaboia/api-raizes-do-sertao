package com.raizesdonordeste.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raizesdonordeste.api.application.service.ProdutoService;
import com.raizesdonordeste.api.controller.dto.ProdutoDTO;

@RestController 
@RequestMapping ("/produtos")
public class ProdutoController {
    
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping 
    public ResponseEntity<List<ProdutoDTO>> listar() {
        List<ProdutoDTO> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }
}
