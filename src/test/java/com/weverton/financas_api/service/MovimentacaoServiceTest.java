package com.weverton.financas_api.service;

import com.weverton.financas_api.dto.MovimentacaoRequestDTO;
import com.weverton.financas_api.dto.MovimentacaoResponseDTO;
import com.weverton.financas_api.model.Categoria;
import com.weverton.financas_api.model.Movimentacao;
import com.weverton.financas_api.model.TipoMovimentacao;
import com.weverton.financas_api.model.Usuario;
import com.weverton.financas_api.repository.CategoriaRepository;
import com.weverton.financas_api.repository.MovimentacaoRepository;
import com.weverton.financas_api.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MovimentacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @InjectMocks
    private MovimentacaoService movimentacaoService;

    @Test
    void deveCriarMovimentacaoComSucesso(){
        Usuario usuarioFake = new Usuario();
        usuarioFake.setId(1L);

        Categoria categoriaFake = new Categoria();
        categoriaFake.setId(2L);
        categoriaFake.setNome("Alimentação");

        MovimentacaoRequestDTO dadosFake = new MovimentacaoRequestDTO();
        dadosFake.setIdUsuario(1L);
        dadosFake.setIdCategoria(2L);
        dadosFake.setDescricao("Mercado");
        dadosFake.setValor(new BigDecimal("150.00"));
        dadosFake.setData(LocalDate.now());

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioFake));

        Mockito.when(categoriaRepository.findById(2L))
                .thenReturn(Optional.of(categoriaFake));

        Movimentacao movimentacaoSalvaFake = new Movimentacao();
        movimentacaoSalvaFake.setId(10L);
        movimentacaoSalvaFake.setUsuario(usuarioFake);
        movimentacaoSalvaFake.setCategoria(categoriaFake);
        movimentacaoSalvaFake.setDescricao("Mercado");
        movimentacaoSalvaFake.setValor(new BigDecimal("150.00"));
        movimentacaoSalvaFake.setData(LocalDate.now());

        Mockito.when(movimentacaoRepository.save(Mockito.any(Movimentacao.class)))
                .thenReturn(movimentacaoSalvaFake);

        MovimentacaoResponseDTO resultado = movimentacaoService.criarMovimentacao(dadosFake);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Mercado", resultado.getDescricao());
        Assertions.assertEquals("Alimentação", resultado.getNomeCategoria());
    }

    @Test
    void deveListarMovimentacoesDoUsuario() {
        Usuario usuarioFake = new Usuario();
        usuarioFake.setId(1L);

        Categoria categoriaFake = new Categoria();
        categoriaFake.setId(2L);
        categoriaFake.setNome("Alimentação");

        Movimentacao mov1 = new Movimentacao();
        mov1.setId(10L);
        mov1.setUsuario(usuarioFake);
        mov1.setCategoria(categoriaFake);
        mov1.setDescricao("Mercado");
        mov1.setValor(new BigDecimal("150.00"));
        mov1.setData(LocalDate.now());

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioFake));

        Mockito.when(movimentacaoRepository.findByUsuario(usuarioFake))
                .thenReturn(List.of(mov1));

        List<MovimentacaoResponseDTO> resultado = movimentacaoService.listarMovimentacao(1L);

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Mercado", resultado.get(0).getDescricao());
    }

    @Test
    void deveListarPorPeriodo() {
        Usuario usuarioFake = new Usuario();
        usuarioFake.setId(1L);

        Categoria categoriaFake = new Categoria();
        categoriaFake.setId(2L);
        categoriaFake.setNome("Transporte");

        Movimentacao mov1 = new Movimentacao();
        mov1.setId(11L);
        mov1.setUsuario(usuarioFake);
        mov1.setCategoria(categoriaFake);
        mov1.setDescricao("Combustível");
        mov1.setValor(new BigDecimal("200.00"));
        mov1.setData(LocalDate.now());

        LocalDate inicio = LocalDate.now().minusDays(10);
        LocalDate fim = LocalDate.now();

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioFake));

        Mockito.when(movimentacaoRepository.findByUsuarioAndDataBetween(usuarioFake, inicio, fim))
                .thenReturn(List.of(mov1));

        List<MovimentacaoResponseDTO> resultado = movimentacaoService.listarPorPeriodo(1L, inicio, fim);

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Combustível", resultado.get(0).getDescricao());
    }

    @Test
    void deveListarPorTipo() {
        Usuario usuarioFake = new Usuario();
        usuarioFake.setId(1L);

        Categoria categoriaFake = new Categoria();
        categoriaFake.setId(2L);
        categoriaFake.setNome("Salário");
        categoriaFake.setTipo(TipoMovimentacao.RECEITA);

        Movimentacao mov1 = new Movimentacao();
        mov1.setId(12L);
        mov1.setUsuario(usuarioFake);
        mov1.setCategoria(categoriaFake);
        mov1.setDescricao("Pagamento mensal");
        mov1.setValor(new BigDecimal("3000.00"));
        mov1.setData(LocalDate.now());

        Mockito.when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioFake));

        Mockito.when(movimentacaoRepository.findByUsuarioAndCategoriaTipo(usuarioFake, TipoMovimentacao.RECEITA))
                .thenReturn(List.of(mov1));

        List<MovimentacaoResponseDTO> resultado = movimentacaoService.listarPorTipo(1L, TipoMovimentacao.RECEITA);

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Pagamento mensal", resultado.get(0).getDescricao());
    }

    @Test
    void deveDeletarMovimentacaoComSucesso() {
        Usuario usuarioFake = new Usuario();
        usuarioFake.setId(1L);

        Movimentacao mov1 = new Movimentacao();
        mov1.setId(10L);
        mov1.setUsuario(usuarioFake);

        Mockito.when(movimentacaoRepository.findById(10L))
                .thenReturn(Optional.of(mov1));

        movimentacaoService.deletarMovimentacao(10L, 1L);

        Mockito.verify(movimentacaoRepository, Mockito.times(1)).deleteById(10L);
    }

    @Test
    void deveLancarExcecaoAoDeletarMovimentacaoDeOutroUsuario() {
        Usuario donoDaMovimentacao = new Usuario();
        donoDaMovimentacao.setId(1L);

        Movimentacao mov1 = new Movimentacao();
        mov1.setId(10L);
        mov1.setUsuario(donoDaMovimentacao);

        Mockito.when(movimentacaoRepository.findById(10L))
                .thenReturn(Optional.of(mov1));

        // idUsuario = 99L, diferente do dono (1L)
        RuntimeException excecao = Assertions.assertThrows(
                RuntimeException.class,
                () -> movimentacaoService.deletarMovimentacao(10L, 99L)
        );

        Assertions.assertEquals(
                "Essa ação não pode ser comcluida! Id não pertence a esse usuario.",
                excecao.getMessage()
        );

        Mockito.verify(movimentacaoRepository, Mockito.never()).deleteById(Mockito.any());
    }

    @Test
    void deveAtualizarMovimentacaoComSucesso() {
        Usuario usuarioFake = new Usuario();
        usuarioFake.setId(1L);

        Categoria categoriaAntiga = new Categoria();
        categoriaAntiga.setId(2L);
        categoriaAntiga.setNome("Alimentação");

        Categoria categoriaNova = new Categoria();
        categoriaNova.setId(3L);
        categoriaNova.setNome("Lazer");

        Movimentacao movimentacaoExistente = new Movimentacao();
        movimentacaoExistente.setId(10L);
        movimentacaoExistente.setUsuario(usuarioFake);
        movimentacaoExistente.setCategoria(categoriaAntiga);
        movimentacaoExistente.setDescricao("Mercado");
        movimentacaoExistente.setValor(new BigDecimal("150.00"));
        movimentacaoExistente.setData(LocalDate.now());

        MovimentacaoRequestDTO dadosNovos = new MovimentacaoRequestDTO();
        dadosNovos.setIdCategoria(3L);
        dadosNovos.setDescricao("Cinema");
        dadosNovos.setValor(new BigDecimal("80.00"));
        dadosNovos.setData(LocalDate.now());

        Mockito.when(movimentacaoRepository.findById(10L))
                .thenReturn(Optional.of(movimentacaoExistente));

        Mockito.when(categoriaRepository.findById(3L))
                .thenReturn(Optional.of(categoriaNova));

        Mockito.when(movimentacaoRepository.save(Mockito.any(Movimentacao.class)))
                .thenReturn(movimentacaoExistente);

        MovimentacaoResponseDTO resultado = movimentacaoService.atualizarMovimentacao(10L, 1L, dadosNovos);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Cinema", resultado.getDescricao());
        Assertions.assertEquals("Lazer", resultado.getNomeCategoria());
    }
}
