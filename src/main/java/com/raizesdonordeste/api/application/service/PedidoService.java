package com.raizesdonordeste.api.application.service;

import com.raizesdonordeste.api.controller.dto.CriarPedidoRequestDTO;
import com.raizesdonordeste.api.controller.dto.ItemPedidoRequestDTO;
import com.raizesdonordeste.api.controller.dto.PagamentoRequestDTO;
import com.raizesdonordeste.api.controller.dto.PagamentoResponseDTO;
import com.raizesdonordeste.api.controller.dto.PedidoResponseDTO;
import com.raizesdonordeste.api.domain.enums.CanalPedido;
import com.raizesdonordeste.api.domain.enums.StatusPedido;
import com.raizesdonordeste.api.domain.exception.RegraNegocioException;
import com.raizesdonordeste.api.domain.model.EstoqueUnidade;
import com.raizesdonordeste.api.domain.model.ItemPedido;
import com.raizesdonordeste.api.domain.model.Pedido;
import com.raizesdonordeste.api.domain.model.Produto;
import com.raizesdonordeste.api.domain.model.Unidade;
import com.raizesdonordeste.api.domain.model.Usuario;
import com.raizesdonordeste.api.infrastructure.mock.PagamentoGateway;
import com.raizesdonordeste.api.infrastructure.repository.EstoqueUnidadeRepository;
import com.raizesdonordeste.api.infrastructure.repository.PedidoRepository;
import com.raizesdonordeste.api.infrastructure.repository.ProdutoRepository;
import com.raizesdonordeste.api.infrastructure.repository.UnidadeRepository;
import com.raizesdonordeste.api.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

        private final PedidoRepository pedidoRepository;
        private final UnidadeRepository unidadeRepository;
        private final UsuarioRepository usuarioRepository;
        private final ProdutoRepository produtoRepository;
        private final EstoqueUnidadeRepository estoqueUnidadeRepository;
        private final PagamentoGateway pagamentoGateway;
        private final AuditoriaService auditoriaService;

        @Transactional
        public PedidoResponseDTO criarPedido(CriarPedidoRequestDTO dto) {
                Unidade unidade = unidadeRepository.findById(dto.getUnidadeId())
                                .orElseThrow(() -> new RuntimeException("Unidade não encontrada."));

                Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

                Pedido pedido = new Pedido();
                pedido.setUnidade(unidade);
                pedido.setUsuario(usuario);
                pedido.setCanalPedido(dto.getCanalPedido());
                pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
                pedido.setValorTotal(BigDecimal.ZERO);

                BigDecimal valorTotalCalculado = BigDecimal.ZERO;
                List<ItemPedido> itensPedido = new ArrayList<>();

                for (ItemPedidoRequestDTO itemDto : dto.getItens()) {
                        Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Produto não encontrado ID: " + itemDto.getProdutoId()));

                        EstoqueUnidade estoque = estoqueUnidadeRepository
                                        .findByUnidadeIdAndProdutoId(unidade.getId(), produto.getId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Produto sem registro de estoque nesta unidade."));

                        if (estoque.getQuantidade() < itemDto.getQuantidade()) {
                                throw new RegraNegocioException(
                                                "ESTOQUE_INSUFICIENTE",
                                                "Não há quantidade suficiente para um ou mais itens.",
                                                "itens.quantidade",
                                                "Produto: " + produto.getNome() + " | Disponível: "
                                                                + estoque.getQuantidade());
                        }

                        estoque.setQuantidade(estoque.getQuantidade() - itemDto.getQuantidade());
                        estoqueUnidadeRepository.save(estoque);

                        ItemPedido item = new ItemPedido();
                        item.setPedido(pedido);
                        item.setProduto(produto);
                        item.setQuantidade(itemDto.getQuantidade());
                        item.setPrecoUnitario(produto.getPrecoBase());
                        itensPedido.add(item);

                        BigDecimal subtotal = produto.getPrecoBase()
                                        .multiply(BigDecimal.valueOf(itemDto.getQuantidade()));
                        valorTotalCalculado = valorTotalCalculado.add(subtotal);
                }

                pedido.setItens(itensPedido);
                pedido.setValorTotal(valorTotalCalculado);

                Pedido pedidoSalvo = pedidoRepository.save(pedido);

                auditoriaService.registrar(
                                "PEDIDO_CRIADO",
                                usuario.getEmail(),
                                "Pedido ID " + pedidoSalvo.getId() + " criado via canal " + pedidoSalvo.getCanalPedido()
                                                + " | Total: R$ " + pedidoSalvo.getValorTotal());

                return new PedidoResponseDTO(pedidoSalvo);
        }

        public List<PedidoResponseDTO> listarPedidos(CanalPedido canalPedido) {
                List<Pedido> pedidos = (canalPedido != null)
                                ? pedidoRepository.findByCanalPedido(canalPedido)
                                : pedidoRepository.findAll();

                return pedidos.stream()
                                .map(PedidoResponseDTO::new)
                                .toList();
        }

        @Transactional
        public PagamentoResponseDTO processarPagamento(Long pedidoId, PagamentoRequestDTO dto) {
                Pedido pedido = pedidoRepository.findById(pedidoId)
                                .orElseThrow(() -> new RuntimeException("Pedido não encontrado ID: " + pedidoId));

                if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
                        throw new RegraNegocioException(
                                        "STATUS_PEDIDO_INVALIDO",
                                        "Este pedido não está aguardando pagamento.",
                                        "status",
                                        "Status atual: " + pedido.getStatus());
                }

                var resultado = pagamentoGateway.processarPagamento(
                                pedido.getId(),
                                pedido.getValorTotal(),
                                dto.getFormaPagamento(),
                                dto.isSimularRecusa());

                if (resultado.aprovado()) {
                        pedido.setStatus(StatusPedido.EM_PREPARO);
                        pedido = pedidoRepository.save(pedido);
                }

                auditoriaService.registrar(
                                resultado.aprovado() ? "PAGAMENTO_APROVADO" : "PAGAMENTO_RECUSADO",
                                pedido.getUsuario().getEmail(),
                                "Pedido ID " + pedido.getId() + " | Transação: " + resultado.transacaoId()
                                                + " | Status: " + pedido.getStatus());

                return new PagamentoResponseDTO(
                                pedido.getId(),
                                resultado.transacaoId(),
                                resultado.aprovado(),
                                pedido.getStatus(),
                                resultado.mensagem());
        }
}