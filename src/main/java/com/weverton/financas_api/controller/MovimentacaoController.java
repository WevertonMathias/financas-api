package com.weverton.financas_api.controller;

import com.weverton.financas_api.dto.MovimentacaoRequestDTO;
import com.weverton.financas_api.dto.MovimentacaoResponseDTO;
import com.weverton.financas_api.model.TipoMovimentacao;
import com.weverton.financas_api.model.Usuario;
import com.weverton.financas_api.service.MovimentacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;
    @PostMapping
    public ResponseEntity<MovimentacaoResponseDTO> criarMovimentacao(@AuthenticationPrincipal Usuario usuario, @RequestBody MovimentacaoRequestDTO dados){
        dados.setIdUsuario(usuario.getId());
        MovimentacaoResponseDTO movimentacaoSalva = movimentacaoService.criarMovimentacao(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoSalva);
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoResponseDTO>> listarMovimentacaoes(@AuthenticationPrincipal Usuario usuario){
        List<MovimentacaoResponseDTO> listaDeMovimentacoes = movimentacaoService.listarMovimentacao(usuario.getId());
        return ResponseEntity.ok(listaDeMovimentacoes);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<MovimentacaoResponseDTO>> listarPorPeriodo(@AuthenticationPrincipal Usuario usuario, @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim){
        List<MovimentacaoResponseDTO> resposta = movimentacaoService.listarPorPeriodo(usuario.getId(), dataInicio, dataFim);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/tipo")
    public ResponseEntity<List<MovimentacaoResponseDTO>> listarPorTipo(@AuthenticationPrincipal Usuario usuario, @RequestParam TipoMovimentacao tipo){
        List<MovimentacaoResponseDTO> resposta = movimentacaoService.listarPorTipo(usuario.getId(), tipo);
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{idMovimentacao}")
    public ResponseEntity<MovimentacaoResponseDTO> atualizarMovimentacao(@PathVariable Long idMovimentacao, @AuthenticationPrincipal Usuario usuario, @RequestBody MovimentacaoRequestDTO dados){
        MovimentacaoResponseDTO movimentacaoAtualizada = movimentacaoService.atualizarMovimentacao(idMovimentacao, usuario.getId(), dados);
        return ResponseEntity.ok(movimentacaoAtualizada);
    }

    @DeleteMapping("/{idMovimentacao}")
    public ResponseEntity<Void> deletarMovimentacao(@PathVariable Long idMovimentacao, @AuthenticationPrincipal Usuario usuario){
        movimentacaoService.deletarMovimentacao(idMovimentacao, usuario.getId());
        return ResponseEntity.noContent().build();
    }
}
