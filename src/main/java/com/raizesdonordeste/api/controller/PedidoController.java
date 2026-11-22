package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.application.service.PedidoService;
import com.raizesdonordeste.api.controller.dto.CriarPedidoRequestDTO;
import com.raizesdonordeste.api.controller.dto.PagamentoRequestDTO;
import com.raizesdonordeste.api.controller.dto.PagamentoResponseDTO;
import com.raizesdonordeste.api.controller.dto.PedidoResponseDTO;
import com.raizesdonordeste.api.domain.enums.CanalPedido;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid CriarPedidoRequestDTO dto) {
        PedidoResponseDTO response = pedidoService.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint: GET http://localhost:8080/pedidos?canalPedido=TOTEM
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar(
            @RequestParam(required = false) CanalPedido canalPedido) {
        return ResponseEntity.ok(pedidoService.listarPedidos(canalPedido));
    }

    // Endpoint: POST http://localhost:8080/pedidos/1/pagamento
    @PostMapping("/{id}/pagamento")
    public ResponseEntity<PagamentoResponseDTO> pagar(
            @PathVariable Long id,
            @RequestBody @Valid PagamentoRequestDTO dto) {
        return ResponseEntity.ok(pedidoService.processarPagamento(id, dto));
    }
}