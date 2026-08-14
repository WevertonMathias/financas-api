package com.weverton.financas_api.exception;

public class RecursoNaoEncontradoException extends RuntimeException{

    public RecursoNaoEncontradoException(String mensagem){
        super(mensagem);
    }
}
