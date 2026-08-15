package com.weverton.financas_api.service;

import com.weverton.financas_api.dto.MovimentacaoResponseDTO;
import com.weverton.financas_api.dto.RelatorioPorCategoriaDTO;
import com.weverton.financas_api.dto.RelatorioSaldoDTO;
import com.weverton.financas_api.model.TipoMovimentacao;
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

@ExtendWith(MockitoExtension.class)
public class RelatorioServiceTest {

    @Mock
    private MovimentacaoService movimentacaoService;

    @InjectMocks
    private RelatorioService relatorioService;

    @Test
    void deveCalcularSaldoDoPeriodoComSucesso() {
        LocalDate inicio = LocalDate.of(2026, 8, 1);
        LocalDate fim = LocalDate.of(2026, 8, 31);

        MovimentacaoResponseDTO receita1 = new MovimentacaoResponseDTO();
        receita1.setValor(new BigDecimal("1000.00"));

        MovimentacaoResponseDTO despesa1 = new MovimentacaoResponseDTO();
        despesa1.setValor(new BigDecimal("300.00"));

        Mockito.when(movimentacaoService.listarPorTipoEPeriodo(1L, TipoMovimentacao.RECEITA, inicio, fim))
                .thenReturn(List.of(receita1));

        Mockito.when(movimentacaoService.listarPorTipoEPeriodo(1L, TipoMovimentacao.DESPESA, inicio, fim))
                .thenReturn(List.of(despesa1));

        RelatorioSaldoDTO resultado = relatorioService.calcularSaldoDoPeriodo(1L, inicio, fim);

        Assertions.assertEquals(new BigDecimal("1000.00"), resultado.getTotalReceitas());
        Assertions.assertEquals(new BigDecimal("300.00"), resultado.getTotalDespesas());
        Assertions.assertEquals(new BigDecimal("700.00"), resultado.getSaldo());
    }

    @Test
    void deveCalcularTotalPorCategoriaComSucesso() {
        LocalDate inicio = LocalDate.of(2026, 8, 1);
        LocalDate fim = LocalDate.of(2026, 8, 31);

        MovimentacaoResponseDTO mov1 = new MovimentacaoResponseDTO();
        mov1.setNomeCategoria("Alimentação");
        mov1.setValor(new BigDecimal("200.00"));

        MovimentacaoResponseDTO mov2 = new MovimentacaoResponseDTO();
        mov2.setNomeCategoria("Alimentação");
        mov2.setValor(new BigDecimal("100.00"));

        MovimentacaoResponseDTO mov3 = new MovimentacaoResponseDTO();
        mov3.setNomeCategoria("Transporte");
        mov3.setValor(new BigDecimal("150.00"));

        Mockito.when(movimentacaoService.listarPorTipoEPeriodo(1L, TipoMovimentacao.DESPESA, inicio, fim))
                .thenReturn(List.of(mov1, mov2, mov3));

        RelatorioPorCategoriaDTO resultado = relatorioService.totalPorCategoria(1L, TipoMovimentacao.DESPESA, inicio, fim);

        Assertions.assertEquals(new BigDecimal("300.00"), resultado.getTotalPorCategoria().get("Alimentação"));
        Assertions.assertEquals(new BigDecimal("150.00"), resultado.getTotalPorCategoria().get("Transporte"));
    }
}
