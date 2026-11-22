package com.raizesdonordeste.api.infrastructure;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.raizesdonordeste.api.domain.enums.PerfilUsuario;
import com.raizesdonordeste.api.domain.model.EstoqueUnidade;
import com.raizesdonordeste.api.domain.model.Produto;
import com.raizesdonordeste.api.domain.model.Unidade;
import com.raizesdonordeste.api.domain.model.Usuario;
import com.raizesdonordeste.api.infrastructure.repository.EstoqueUnidadeRepository;
import com.raizesdonordeste.api.infrastructure.repository.ProdutoRepository;
import com.raizesdonordeste.api.infrastructure.repository.UnidadeRepository;
import com.raizesdonordeste.api.infrastructure.repository.UsuarioRepository;

@Component 
public class DatabaseSeeder implements CommandLineRunner{
    
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueUnidadeRepository estoqueUnidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UnidadeRepository unidadeRepository, ProdutoRepository produtoRepository,
            EstoqueUnidadeRepository estoqueUnidadeRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.unidadeRepository = unidadeRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueUnidadeRepository = estoqueUnidadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void run(String... args) throws Exception{

        Unidade recife = new Unidade(null, "Unidade Recife - Matriz", "Rua do Sol, 1010", true);
        unidadeRepository.save(recife);

        Produto cuscuz = new Produto(null, "Cuscuz Recheado com Carne de Sol", "Cuscuz milho com carne de sol e queijo coalho", new BigDecimal("18.50"), false);
        Produto tapioca = new Produto(null, "Tapioca de Manteiga de Garrafa", "Tapioca tradicional recheada com queijo e manteiga", new BigDecimal("12.00"), false);
        Produto boloMacaxeira = new Produto(null, "Bolo de Macaxeira", "Fatia de bolo artesanal de macaxeira", new BigDecimal("8.00"), false);

        produtoRepository.save(cuscuz);
        produtoRepository.save(tapioca);
        produtoRepository.save(boloMacaxeira);

        estoqueUnidadeRepository.save(new EstoqueUnidade(null, recife, cuscuz, 50));
        estoqueUnidadeRepository.save(new EstoqueUnidade(null, recife, tapioca, 30));
        estoqueUnidadeRepository.save(new EstoqueUnidade(null, recife, boloMacaxeira, 20));

        Usuario admin = new Usuario(null, "Administrador Raízes", "admin@raizes.com.br", passwordEncoder.encode("123456"), PerfilUsuario.ADMIN, true);
        Usuario cliente = new Usuario(null, "Maria Silva", "maria@email.com", passwordEncoder.encode("123456"), PerfilUsuario.CLIENTE, true);

        usuarioRepository.save(admin);
        usuarioRepository.save(cliente);

        System.out.println("✅ BANCO DE DADOS POPULADO COM SUCESSO!");
    }
}
