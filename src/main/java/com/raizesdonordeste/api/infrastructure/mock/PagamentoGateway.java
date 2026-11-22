package com.raizesdonordeste.api.infrastructure.mock;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
public class PagamentoGateway {

    public record ResultadoPagamento(boolean aprovado, String transacaoId, String mensagem) {}

    public ResultadoPagamento processarPagamento(Long pedidoId, BigDecimal valor, String metodoPagamento, boolean simularRecusa) {
        String transacaoId = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        if (simularRecusa) {
            return new ResultadoPagamento(
                false,
                transacaoId,
                "Pagamento recusado pela operadora externa (Simulação)."
            );
        }

        return new ResultadoPagamento(
            true,
            transacaoId,
            "Pagamento de R$ " + valor + " via " + metodoPagamento + " aprovado com sucesso."
        );
    }
}