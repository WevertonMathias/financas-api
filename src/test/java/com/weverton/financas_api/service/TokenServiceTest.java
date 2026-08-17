package com.weverton.financas_api.service;

import com.weverton.financas_api.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "c8f7a3e1b9d4f0e2a6c5b8d7f1e4a2c9b6d3f8e5a1c7b4d9f2e6a3c8b5d1f4e7");
        ReflectionTestUtils.setField(tokenService, "expiration", 86400000L);
    }

    @Test
    void deveGerarTokenValidoParaUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("weverton@email.com");

        String token = tokenService.gerarToken(usuario);

        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());
    }

    @Test
    void deveValidarTokenERetornarIdDoUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("weverton@email.com");

        String token = tokenService.gerarToken(usuario);
        String idExtraido = tokenService.validarToken(token);

        Assertions.assertEquals("1", idExtraido);
    }

    @Test
    void deveRetornarNuloParaTokenInvalido() {
        String idExtraido = tokenService.validarToken("token-completamente-invalido");

        Assertions.assertNull(idExtraido);
    }
}
