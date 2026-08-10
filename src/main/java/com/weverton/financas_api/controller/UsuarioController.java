package com.weverton.financas_api.controller;

import com.weverton.financas_api.dto.AtualizarPerfilRequestDTO;
import com.weverton.financas_api.dto.LoginRequestDTO;
import com.weverton.financas_api.dto.UsuarioRequestDTO;
import com.weverton.financas_api.dto.UsuarioResponseDTO;
import com.weverton.financas_api.model.Usuario;
import com.weverton.financas_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public ResponseEntity <UsuarioResponseDTO> registrarUsuario(@RequestBody UsuarioRequestDTO dados){
         UsuarioResponseDTO usuarioSalvo = service.registrarUsuario(dados);
         return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> logarUsuario(@RequestBody LoginRequestDTO login){
        UsuarioResponseDTO loginUsuario = service.logarUsuario(login);
        return ResponseEntity.ok(loginUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id){
         UsuarioResponseDTO usuarioEncontrado = service.buscarPorId(id);
         return ResponseEntity.ok(usuarioEncontrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable Long id, @RequestBody AtualizarPerfilRequestDTO dadosAtualizados){
        UsuarioResponseDTO usuarioAtualizado = service.atualizarUsuario(id, dadosAtualizados);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, @RequestParam String senhaAtual, @RequestParam String novaSenha){
        service.alterarSenha(id, senhaAtual, novaSenha);
        return ResponseEntity.noContent().build();
    }

}
