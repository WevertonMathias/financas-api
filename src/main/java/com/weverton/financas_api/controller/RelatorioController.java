package com.weverton.financas_api.controller;

import com.weverton.financas_api.dto.RelatorioPorCategoriaDTO;
import com.weverton.financas_api.dto.RelatorioSaldoDTO;
import com.weverton.financas_api.model.TipoMovimentacao;
import com.weverton.financas_api.model.Usuario;
import com.weverton.financas_api.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/saldo")
    public ResponseEntity<RelatorioSaldoDTO> saldoDoPeriodo(@AuthenticationPrincipal Usuario usuario, @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        RelatorioSaldoDTO relatorio = relatorioService.calcularSaldoDoPeriodo(usuario.getId(), dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/categoria")
    public ResponseEntity<RelatorioPorCategoriaDTO> totalPorCategoria(@AuthenticationPrincipal Usuario usuario, @RequestParam TipoMovimentacao tipo, @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim){
        RelatorioPorCategoriaDTO relatorio = relatorioService.totalPorCategoria(usuario.getId(), tipo, dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }
}
