package com.raizesdonordeste.api.domain.exception;

import lombok.Getter;

@Getter
public class RegraNegocioException extends RuntimeException {
    private final String codigoErro;
    private final String campo;
    private final String detalhe;

    public RegraNegocioException(String codigoErro, String message, String campo, String detalhe) {
        super(message);
        this.codigoErro = codigoErro;
        this.campo = campo;
        this.detalhe = detalhe;
    }
}