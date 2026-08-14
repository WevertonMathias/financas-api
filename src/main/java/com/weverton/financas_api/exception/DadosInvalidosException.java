package com.weverton.financas_api.exception;

public class DadosInvalidosException extends RuntimeException{

    public DadosInvalidosException(String mensagem){
        super(mensagem);
    }
}
