package com.weverton.financas_api.controller;

import com.weverton.financas_api.dto.MovimentacaoRequestDTO;
import com.weverton.financas_api.dto.MovimentacaoResponseDTO;
import com.weverton.financas_api.model.TipoMovimentacao;
import com.weverton.financas_api.service.MovimentacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    @PostMapping
    public ResponseEntity<MovimentacaoResponseDTO> criarMovimentacao(@RequestBody MovimentacaoRequestDTO dados ){
        MovimentacaoResponseDTO movimentacaoSalva = movimentacaoService.criarMovimentacao(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoSalva);
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoResponseDTO>> listarMovimentacaoes(@RequestParam Long idUsuario){
        List<MovimentacaoResponseDTO> listaDeMovimentacoes = movimentacaoService.listarMovimentacao(idUsuario);
        return ResponseEntity.ok(listaDeMovimentacoes);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<MovimentacaoResponseDTO>> listarPorPeriodo(@RequestParam Long idUsuario, @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim){
        List<MovimentacaoResponseDTO> listaDePeriodo = movimentacaoService.listarPorPeriodo(idUsuario, dataInicio, dataFim);
        return ResponseEntity.ok(listaDePeriodo);
    }

    @GetMapping("/tipo")
    public ResponseEntity<List<MovimentacaoResponseDTO>> listarPorTipo(@RequestParam Long idUsuario, @RequestParam TipoMovimentacao tipo){
        List<MovimentacaoResponseDTO> listaDeTipos = movimentacaoService.listarPorTipo(idUsuario, tipo);
        return ResponseEntity.ok(listaDeTipos);
    }

    @PutMapping("/{idMovimentacao}")
    public ResponseEntity<MovimentacaoResponseDTO> atualizarMovimentacao(@PathVariable Long idMovimentacao, @RequestParam Long idUsuario, @RequestBody MovimentacaoRequestDTO dados){
        MovimentacaoResponseDTO movimentacaoAtualizada = movimentacaoService.atualizarMovimentacao(idMovimentacao, idUsuario, dados);
        return ResponseEntity.ok(movimentacaoAtualizada);
    }

    @DeleteMapping("/{idMovimentacao}")
    public ResponseEntity<Void> deletarMovimentacao(@PathVariable Long idMovimentacao, @RequestParam Long idUsuario){
         movimentacaoService.deletarMovimentacao(idMovimentacao, idUsuario);
         return ResponseEntity.noContent().build();
    }
}
