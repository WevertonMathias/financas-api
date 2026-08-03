package com.weverton.financas_api.service;

import com.weverton.financas_api.model.Usuario;
import com.weverton.financas_api.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveRegistrarUsuarioComSucesso() {
        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setNome("Weverton");
        usuarioNovo.setEmail("weverton@email.com");
        usuarioNovo.setSenha("123456");

        Mockito.when(usuarioRepository.existsByEmail(usuarioNovo.getEmail()))
                .thenReturn(false);

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class)))
                .thenReturn(usuarioNovo);

        Usuario usuarioSalvo = usuarioService.registrarUsuario(usuarioNovo);

        Assertions.assertNotNull(usuarioSalvo);
        Assertions.assertEquals("Weverton", usuarioSalvo.getNome());
        Assertions.assertEquals("weverton@email.com", usuarioSalvo.getEmail());

        Mockito.verify(usuarioRepository, Mockito.times(1))
                .save(Mockito.any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setEmail("weverton@email.com");

        Mockito.when(usuarioRepository.existsByEmail(usuarioExistente.getEmail()))
                .thenReturn(true);

        RuntimeException excecao = Assertions.assertThrows(
                RuntimeException.class,
                () -> usuarioService.registrarUsuario(usuarioExistente)
        );

        Assertions.assertEquals("Este e-mail já existe!", excecao.getMessage());

        Mockito.verify(usuarioRepository, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void deveLogarUsuarioComSucesso() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaPura = "123456";

        Usuario usuarioBanco = new Usuario();
        usuarioBanco.setEmail("weverton@email.com");
        usuarioBanco.setSenha(encoder.encode(senhaPura));

        Mockito.when(usuarioRepository.findByEmail("weverton@email.com"))
                .thenReturn(Optional.of(usuarioBanco));

        Usuario usuarioLogado = usuarioService.logarUsuario("weverton@email.com", senhaPura);

        Assertions.assertNotNull(usuarioLogado);
        Assertions.assertEquals("weverton@email.com", usuarioLogado.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoSenhaEstiverIncorreta() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario usuarioBanco = new Usuario();
        usuarioBanco.setEmail("weverton@email.com");
        usuarioBanco.setSenha(encoder.encode("123456"));

        Mockito.when(usuarioRepository.findByEmail("weverton@email.com"))
                .thenReturn(Optional.of(usuarioBanco));

        RuntimeException excecao = Assertions.assertThrows(
                RuntimeException.class,
                () -> usuarioService.logarUsuario("weverton@email.com", "senhaErrada")
        );

        Assertions.assertEquals("E-mail ou senha invalidos!", excecao.getMessage());
    }

    @Test
    void deveBuscarUsuarioPorIdComSucesso() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNome("Weverton");

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        Usuario usuarioEncontrado = usuarioService.buscarPorId(1L);

        Assertions.assertNotNull(usuarioEncontrado);
        Assertions.assertEquals("Weverton", usuarioEncontrado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExiste() {
        Mockito.when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException excecao = Assertions.assertThrows(
                RuntimeException.class,
                () -> usuarioService.buscarPorId(99L)
        );

        Assertions.assertEquals("Usuário não encontrado!", excecao.getMessage());
    }
}