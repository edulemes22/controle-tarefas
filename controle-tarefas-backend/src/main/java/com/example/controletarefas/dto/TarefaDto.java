package com.example.controletarefas.dto;

import com.example.controletarefas.model.Tarefa;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TarefaDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Criacao {
        @NotBlank
        @Size(max = 100)
        private String nome;

        @Size(max = 500)
        private String descricao;

        @NotNull
        private Tarefa.Prioridade prioridade;

        @NotNull
        @FutureOrPresent
        private LocalDate dataPrevistaConclusao;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Atualizacao {
        @NotBlank
        @Size(max = 100)
        private String nome;

        @Size(max = 500)
        private String descricao;

        @NotNull
        private Tarefa.Prioridade prioridade;

        @NotNull
        @FutureOrPresent
        private LocalDate dataPrevistaConclusao;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Resposta {
        private UUID id;
        private String nome;
        private String descricao;
        private Tarefa.Prioridade prioridade;
        private Tarefa.Situacao situacao;
        private LocalDate dataPrevistaConclusao;
        private LocalDateTime dataCriacao;
    }

}
