package com.raizesdonordeste.api.controller.dto;

import com.raizesdonordeste.api.domain.enums.CanalPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CriarPedidoRequestDTO {

    @NotNull(message = "O ID da unidade é obrigatório.")
    private Long unidadeId;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long usuarioId;

    @NotNull(message = "O canal do pedido (canalPedido) é obrigatório.")
    private CanalPedido canalPedido;

    @NotEmpty(message = "O pedido deve conter pelo menos um item.")
    @Valid
    private List<ItemPedidoRequestDTO> itens;
}