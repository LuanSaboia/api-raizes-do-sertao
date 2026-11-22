package com.raizesdonordeste.api.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raizesdonordeste.api.domain.model.EstoqueUnidade;

public interface EstoqueUnidadeRepository extends JpaRepository<EstoqueUnidade, Long>{
    
    Optional<EstoqueUnidade> findByUnidadeIdAndProdutoId(Long unidadeId, Long produtoId);
}
