package com.raizesdonordeste.api.infrastructure.repository;

import com.raizesdonordeste.api.domain.model.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
}