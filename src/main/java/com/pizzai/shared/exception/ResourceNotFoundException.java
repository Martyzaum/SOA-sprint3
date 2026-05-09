package com.pizzai.shared.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String recurso, Object id) {
        super(String.format("%s com identificador '%s' nao encontrado", recurso, id));
    }

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }
}
