package com.raizesdonordeste.api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PagamentoRequestDTO {

    @NotBlank(message = "A forma de pagamento é obrigatória.")
    private String formaPagamento;

    private boolean simularRecusa = false;
}