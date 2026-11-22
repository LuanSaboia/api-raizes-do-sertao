package com.raizesdonordeste.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private UsuarioResumoDTO user;

    @Data
    @AllArgsConstructor
    public static class UsuarioResumoDTO {
        private Long id;
        private String nome;
        private String perfil;
    }
}