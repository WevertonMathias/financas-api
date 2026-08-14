package com.weverton.financas_api.exception;

public class RecursoJaExisteException extends RuntimeException{

    public RecursoJaExisteException(String mensagem){
        super(mensagem);
    }
}
