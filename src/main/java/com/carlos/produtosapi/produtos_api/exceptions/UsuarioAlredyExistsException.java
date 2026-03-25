package com.carlos.produtosapi.produtos_api.exceptions;

public class UsuarioAlredyExistsException extends RuntimeException {
    public UsuarioAlredyExistsException(String message) {
        super(message);
    }
}
