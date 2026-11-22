package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.controller.dto.ErroPadraoDTO;
import com.raizesdonordeste.api.controller.dto.ErroPadraoDTO.DetalheErroDTO;
import com.raizesdonordeste.api.domain.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.api.domain.exception.RegraNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroPadraoDTO> handleNotFound(RecursoNaoEncontradoException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                "RECURSO_NAO_ENCONTRADO",
                ex.getMessage(),
                List.of(),
                Instant.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroPadraoDTO> handleBusinessRule(RegraNegocioException ex, HttpServletRequest request) {
        List<DetalheErroDTO> details = (ex.getCampo() != null)
                ? List.of(new DetalheErroDTO(ex.getCampo(), ex.getDetalhe()))
                : List.of();

        ErroPadraoDTO erro = new ErroPadraoDTO(
                ex.getCodigoErro(),
                ex.getMessage(),
                details,
                Instant.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadraoDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<DetalheErroDTO> details = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new DetalheErroDTO(err.getField(), err.getDefaultMessage()))
                .toList();

        ErroPadraoDTO erro = new ErroPadraoDTO(
                "ERRO_VALIDACAO",
                "Dados inválidos na requisição.",
                details,
                Instant.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroPadraoDTO> handleInvalidJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                "REQUISICAO_INVALIDA",
                "Erro ao ler o JSON. Verifique se campos como 'canalPedido' possuem valores válidos (APP, TOTEM, BALCAO, PICKUP, WEB).",
                List.of(),
                Instant.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}