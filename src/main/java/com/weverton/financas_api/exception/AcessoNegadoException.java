package com.weverton.financas_api.exception;

public class AcessoNegadoException extends RuntimeException{

    public AcessoNegadoException(String mensagem){
        super(mensagem);
    }
}
