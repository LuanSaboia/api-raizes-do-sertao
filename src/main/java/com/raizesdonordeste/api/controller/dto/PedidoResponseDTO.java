package com.raizesdonordeste.api.controller.dto;

import com.raizesdonordeste.api.domain.enums.CanalPedido;
import com.raizesdonordeste.api.domain.enums.StatusPedido;
import com.raizesdonordeste.api.domain.model.Pedido;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PedidoResponseDTO {

    private Long id;
    private Long unidadeId;
    private String nomeUnidade;
    private Long usuarioId;
    private CanalPedido canalPedido;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private LocalDateTime dataCriacao;
    private List<ItemResponseDTO> itens;

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.unidadeId = pedido.getUnidade().getId();
        this.nomeUnidade = pedido.getUnidade().getNome();
        this.usuarioId = pedido.getUsuario().getId();
        this.canalPedido = pedido.getCanalPedido();
        this.status = pedido.getStatus();
        this.valorTotal = pedido.getValorTotal();
        this.dataCriacao = pedido.getDataCriacao();
        this.itens = pedido.getItens().stream()
                .map(ItemResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Data
    public static class ItemResponseDTO {
        private Long produtoId;
        private String nomeProduto;
        private Integer quantidade;
        private BigDecimal precoUnitario;

        public ItemResponseDTO(com.raizesdonordeste.api.domain.model.ItemPedido item) {
            this.produtoId = item.getProduto().getId();
            this.nomeProduto = item.getProduto().getNome();
            this.quantidade = item.getQuantidade();
            this.precoUnitario = item.getPrecoUnitario();
        }
    }
}