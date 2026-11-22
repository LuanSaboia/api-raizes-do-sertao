package com.raizesdonordeste.api.controller.dto;

import com.raizesdonordeste.api.domain.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagamentoResponseDTO {
    private Long pedidoId;
    private String transacaoId;
    private boolean aprovado;
    private StatusPedido statusAtualPedido;
    private String mensagem;
}