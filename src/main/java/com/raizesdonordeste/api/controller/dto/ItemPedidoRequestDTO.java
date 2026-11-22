package com.raizesdonordeste.api.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemPedidoRequestDTO {

    @NotNull(message = "O ID do produto é obrigatório.")
    private Long produtoId;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade mínima é 1.")
    private Integer quantidade;
}