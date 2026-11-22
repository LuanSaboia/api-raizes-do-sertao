package com.raizesdonordeste.api.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raizesdonordeste.api.domain.model.Unidade;

public interface UnidadeRepository extends JpaRepository<Unidade, Long>{
    
}
