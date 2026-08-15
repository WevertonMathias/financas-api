package com.weverton.financas_api.service;

import com.weverton.financas_api.dto.MovimentacaoResponseDTO;
import com.weverton.financas_api.dto.RelatorioPorCategoriaDTO;
import com.weverton.financas_api.dto.RelatorioSaldoDTO;
import com.weverton.financas_api.model.TipoMovimentacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final MovimentacaoService movimentacaoService;

    public RelatorioSaldoDTO calcularSaldoDoPeriodo(Long idUsuario, LocalDate dataInicio, LocalDate dataFim) {

        List<MovimentacaoResponseDTO> receitas = movimentacaoService.listarPorTipoEPeriodo(idUsuario, TipoMovimentacao.RECEITA, dataInicio, dataFim);
        List<MovimentacaoResponseDTO> despesas = movimentacaoService.listarPorTipoEPeriodo(idUsuario, TipoMovimentacao.DESPESA, dataInicio, dataFim);

        BigDecimal totalReceitas = BigDecimal.ZERO;
        for (MovimentacaoResponseDTO dto : receitas) {
            totalReceitas = totalReceitas.add(dto.getValor());
        }

        BigDecimal totalDespesas = BigDecimal.ZERO;
        for (MovimentacaoResponseDTO dto : despesas) {
            totalDespesas = totalDespesas.add(dto.getValor());
        }

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        RelatorioSaldoDTO resposta = new RelatorioSaldoDTO();
        resposta.setTotalReceitas(totalReceitas);
        resposta.setTotalDespesas(totalDespesas);
        resposta.setSaldo(saldo);

        return resposta;
    }

    public RelatorioPorCategoriaDTO totalPorCategoria(Long idUsuario, TipoMovimentacao tipo, LocalDate dataInicio, LocalDate dataFim){

        List<MovimentacaoResponseDTO> movimentacoes = movimentacaoService.listarPorTipoEPeriodo(idUsuario, tipo, dataInicio, dataFim);

        Map<String, BigDecimal> totalPorCategoria = new HashMap<>();

        for (MovimentacaoResponseDTO dto : movimentacoes){
            String categoria = dto.getNomeCategoria();

            if (totalPorCategoria.containsKey(categoria)){
                BigDecimal totalAtual = totalPorCategoria.get(categoria);
                totalPorCategoria.put(categoria, totalAtual.add(dto.getValor()));
            }else {
                totalPorCategoria.put(categoria, dto.getValor());
            }

        }
        RelatorioPorCategoriaDTO resposata = new RelatorioPorCategoriaDTO();
        resposata.setTotalPorCategoria(totalPorCategoria);

        return resposata;

    }
}
