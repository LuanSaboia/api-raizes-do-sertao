package com.raizesdonordeste.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class ErroPadraoDTO {
    private String error;
    private String message;
    private List<DetalheErroDTO> details;
    private Instant timestamp;
    private String path;

    @Data
    @AllArgsConstructor
    public static class DetalheErroDTO {
        private String field;
        private String issue;
    }
}