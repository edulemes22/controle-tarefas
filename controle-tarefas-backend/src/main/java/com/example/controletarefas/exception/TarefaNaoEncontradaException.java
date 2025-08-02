package com.example.controletarefas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TarefaNaoEncontradaException extends RuntimeException {

    public TarefaNaoEncontradaException(UUID id) {
        super(String.format("Tarefa com ID %s não encontrada", id));
    }

    public TarefaNaoEncontradaException(String message) {
        super(message);
    }
}
