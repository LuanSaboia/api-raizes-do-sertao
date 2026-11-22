package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.application.service.AuditoriaService;
import com.raizesdonordeste.api.controller.dto.ErroPadraoDTO;
import com.raizesdonordeste.api.controller.dto.LoginRequestDTO;
import com.raizesdonordeste.api.controller.dto.LoginResponseDTO;
import com.raizesdonordeste.api.domain.model.Usuario;
import com.raizesdonordeste.api.infrastructure.repository.UsuarioRepository;
import com.raizesdonordeste.api.infrastructure.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditoriaService auditoriaService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO dto, HttpServletRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail()).orElse(null);

        if (usuario == null || !passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            auditoriaService.registrar("FALHA_LOGIN", dto.getEmail(), "Tentativa de login com credenciais inválidas.");
            ErroPadraoDTO erro = new ErroPadraoDTO(
                    "CREDENCIAIS_INVALIDAS",
                    "E-mail ou senha inválidos.",
                    List.of(),
                    Instant.now(),
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
        }

        String token = jwtService.gerarToken(usuario);
        auditoriaService.registrar("LOGIN_SUCESSO", usuario.getEmail(), "Login efetuado com perfil " + usuario.getPerfil());

        var userResumo = new LoginResponseDTO.UsuarioResumoDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getPerfil().name()
        );

        return ResponseEntity.ok(new LoginResponseDTO(token, "Bearer", 3600, userResumo));
    }
}