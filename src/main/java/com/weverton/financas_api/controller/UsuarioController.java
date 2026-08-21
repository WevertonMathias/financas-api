package com.weverton.financas_api.controller;

import com.weverton.financas_api.dto.*;
import com.weverton.financas_api.exception.AcessoNegadoException;
import com.weverton.financas_api.model.Usuario;
import com.weverton.financas_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public ResponseEntity <UsuarioResponseDTO> registrarUsuario(@RequestBody UsuarioRequestDTO dados){
         UsuarioResponseDTO usuarioSalvo = service.registrarUsuario(dados);
         return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> logarUsuario(@RequestBody LoginRequestDTO login){
        LoginResponseDTO loginUsuario = service.logarUsuario(login);
        return ResponseEntity.ok(loginUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable Long id){
        if (!usuarioLogado.getId().equals(id)) {
            throw new AcessoNegadoException("Você não pode acessar dados de outro usuário!");
        }
        UsuarioResponseDTO usuarioEncontrado = service.buscarPorId(id);
        return ResponseEntity.ok(usuarioEncontrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable Long id, @RequestBody AtualizarPerfilRequestDTO dadosAtualizados){
        if (!usuarioLogado.getId().equals(id)) {
            throw new AcessoNegadoException("Você não pode alterar dados de outro usuário!");
        }
        UsuarioResponseDTO usuarioAtualizado = service.atualizarUsuario(id, dadosAtualizados);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable Long id, @RequestParam String senhaAtual, @RequestParam String novaSenha){
        if (!usuarioLogado.getId().equals(id)) {
            throw new AcessoNegadoException("Você não pode alterar a senha de outro usuário!");
        }
        service.alterarSenha(id, senhaAtual, novaSenha);
        return ResponseEntity.noContent().build();
    }

}
