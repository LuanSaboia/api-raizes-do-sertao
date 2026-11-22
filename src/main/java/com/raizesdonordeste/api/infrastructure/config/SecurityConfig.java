package com.raizesdonordeste.api.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.raizesdonordeste.api.controller.dto.ErroPadraoDTO;
import com.raizesdonordeste.api.infrastructure.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Instant;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .authorizeHttpRequests(auth -> auth
                
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/produtos/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                .requestMatchers(HttpMethod.GET, "/pedidos/**").hasAnyRole("ADMIN", "ATENDENTE")

                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ErroPadraoDTO erro = new ErroPadraoDTO(
                            "NAO_AUTENTICADO",
                            "Token de autenticação ausente ou inválido.",
                            List.of(),
                            Instant.now(),
                            request.getRequestURI()
                    );
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(erro));
                })
                
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ErroPadraoDTO erro = new ErroPadraoDTO(
                            "ACESSO_NEGADO",
                            "Seu perfil de usuário não possui permissão para acessar este recurso.",
                            List.of(),
                            Instant.now(),
                            request.getRequestURI()
                    );
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(erro));
                })
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}