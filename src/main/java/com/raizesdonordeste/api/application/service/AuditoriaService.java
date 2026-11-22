package com.raizesdonordeste.api.application.service;

import com.raizesdonordeste.api.domain.model.LogAuditoria;
import com.raizesdonordeste.api.infrastructure.repository.LogAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditoriaService {

    private final LogAuditoriaRepository logAuditoriaRepository;

    public void registrar(String acao, String usuario, String detalhes) {
        log.info("[AUDITORIA LGPD] Ação: {} | Usuário: {} | Detalhes: {}", acao, usuario, detalhes);
        logAuditoriaRepository.save(new LogAuditoria(acao, usuario, detalhes));
    }
}