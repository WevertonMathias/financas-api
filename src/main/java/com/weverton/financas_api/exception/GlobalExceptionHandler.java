package com.weverton.financas_api.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<String> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException excecao) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(excecao.getMessage());
    }

    @ExceptionHandler(RecursoJaExisteException.class)
    public ResponseEntity<String> tratarRecursoJaExistente(RecursoJaExisteException excecao){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(excecao.getMessage());
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<String> tratarDadosInvalidos(DadosInvalidosException excecao){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(excecao.getMessage());
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<String> tratarAcessoNegado(AcessoNegadoException excecao){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(excecao.getMessage());
    }

}
