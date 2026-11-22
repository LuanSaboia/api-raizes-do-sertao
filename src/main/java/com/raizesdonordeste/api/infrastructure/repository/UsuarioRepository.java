package com.raizesdonordeste.api.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raizesdonordeste.api.domain.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    Optional<Usuario> findByEmail(String email);
}
