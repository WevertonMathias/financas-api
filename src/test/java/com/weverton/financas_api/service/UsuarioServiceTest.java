package com.weverton.financas_api.service;

import com.weverton.financas_api.dto.*;
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

    // ---------- registrarUsuario ----------

    @Test
    void deveRegistrarUsuarioComSucesso() {
        UsuarioRequestDTO dadosFake = new UsuarioRequestDTO();
        dadosFake.setNome("Weverton");
        dadosFake.setEmail("weverton@email.com");
        dadosFake.setSenha("123456");

        Usuario usuarioSalvoFake = new Usuario();
        usuarioSalvoFake.setId(1L);
        usuarioSalvoFake.setNome("Weverton");
        usuarioSalvoFake.setEmail("weverton@email.com");
        usuarioSalvoFake.setSenha("hashCriptografado");

        Mockito.when(usuarioRepository.existsByEmail("weverton@email.com"))
                .thenReturn(false);

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class)))
                .thenReturn(usuarioSalvoFake);

        UsuarioResponseDTO resultado = usuarioService.registrarUsuario(dadosFake);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Weverton", resultado.getNome());
        Assertions.assertEquals("weverton@email.com", resultado.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {
        UsuarioRequestDTO dadosFake = new UsuarioRequestDTO();
        dadosFake.setNome("Weverton");
        dadosFake.setEmail("weverton@email.com");
        dadosFake.setSenha("123456");

        Mockito.when(usuarioRepository.existsByEmail("weverton@email.com"))
                .thenReturn(true);

        RuntimeException excecao = Assertions.assertThrows(
                RuntimeException.class,
                () -> usuarioService.registrarUsuario(dadosFake)
        );

        Assertions.assertEquals("Este e-mail já existe!", excecao.getMessage());
        Mockito.verify(usuarioRepository, Mockito.never()).save(Mockito.any());
    }

    // ---------- logarUsuario ----------

    @Test
    void deveLogarUsuarioComSucesso() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaPura = "123456";

        Usuario usuarioBanco = new Usuario();
        usuarioBanco.setId(1L);
        usuarioBanco.setNome("Weverton");
        usuarioBanco.setEmail("weverton@email.com");
        usuarioBanco.setSenha(encoder.encode(senhaPura));

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("weverton@email.com");
        login.setSenha(senhaPura);

        Mockito.when(usuarioRepository.findByEmail("weverton@email.com"))
                .thenReturn(Optional.of(usuarioBanco));

        UsuarioResponseDTO resultado = usuarioService.logarUsuario(login);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("weverton@email.com", resultado.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoSenhaEstiverIncorreta() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario usuarioBanco = new Usuario();
        usuarioBanco.setId(1L);
        usuarioBanco.setEmail("weverton@email.com");
        usuarioBanco.setSenha(encoder.encode("123456"));

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("weverton@email.com");
        login.setSenha("senhaErrada");

        Mockito.when(usuarioRepository.findByEmail("weverton@email.com"))
                .thenReturn(Optional.of(usuarioBanco));

        RuntimeException excecao = Assertions.assertThrows(
                RuntimeException.class,
                () -> usuarioService.logarUsuario(login)
        );

        Assertions.assertEquals("E-mail ou senha invalidos!", excecao.getMessage());
    }

    // ---------- buscarPorId ----------

    @Test
    void deveBuscarUsuarioPorIdComSucesso() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNome("Weverton");
        usuarioExistente.setEmail("weverton@email.com");

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1L);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Weverton", resultado.getNome());
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

    // ---------- atualizarUsuario ----------

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNome("Weverton");
        usuarioExistente.setEmail("weverton@email.com");
        usuarioExistente.setSenha("hashAntigo");

        AtualizarPerfilRequestDTO novosDados = new AtualizarPerfilRequestDTO();
        novosDados.setNome("Weverton Mathias");
        novosDados.setEmail("weverton.novo@email.com");

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        Mockito.when(usuarioRepository.existsByEmail("weverton.novo@email.com"))
                .thenReturn(false);

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class)))
                .thenReturn(usuarioExistente);

        UsuarioResponseDTO resultado = usuarioService.atualizarUsuario(1L, novosDados);

        Assertions.assertEquals("Weverton Mathias", resultado.getNome());
        Assertions.assertEquals("weverton.novo@email.com", resultado.getEmail());
    }

    @Test
    void deveLancarExcecaoAoAtualizarComEmailJaUsadoPorOutroUsuario() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setEmail("weverton@email.com");

        AtualizarPerfilRequestDTO novosDados = new AtualizarPerfilRequestDTO();
        novosDados.setNome("Weverton");
        novosDados.setEmail("outro@email.com");

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        Mockito.when(usuarioRepository.existsByEmail("outro@email.com"))
                .thenReturn(true);

        RuntimeException excecao = Assertions.assertThrows(
                RuntimeException.class,
                () -> usuarioService.atualizarUsuario(1L, novosDados)
        );

        Assertions.assertEquals("Este novo e-mail já está em uso por outro usuário!", excecao.getMessage());
    }

    // ---------- alterarSenha ----------

    @Test
    void deveAlterarSenhaComSucesso() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setSenha(encoder.encode("senhaAntiga"));

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        usuarioService.alterarSenha(1L, "senhaAntiga", "senhaNova123");

        Mockito.verify(usuarioRepository, Mockito.times(1)).save(Mockito.any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaAtualEstiverErrada() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setSenha(encoder.encode("senhaCorreta"));

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        RuntimeException excecao = Assertions.assertThrows(
                RuntimeException.class,
                () -> usuarioService.alterarSenha(1L, "senhaErrada", "senhaNova")
        );

        Assertions.assertEquals("A senha atual está incorreta!", excecao.getMessage());
        Mockito.verify(usuarioRepository, Mockito.never()).save(Mockito.any());
    }
}