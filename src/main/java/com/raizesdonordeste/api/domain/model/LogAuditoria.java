package com.raizesdonordeste.api.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs_auditoria")
@Data
@NoArgsConstructor
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String acao;

    @Column(nullable = false)
    private String usuarioResponsavel;

    @Column(nullable = false)
    private String detalhes;

    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

    public LogAuditoria(String acao, String usuarioResponsavel, String detalhes) {
        this.acao = acao;
        this.usuarioResponsavel = usuarioResponsavel;
        this.detalhes = detalhes;
    }
}