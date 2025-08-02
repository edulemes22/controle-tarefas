package com.example.controletarefas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tarefas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarefa {

    public enum Prioridade {
        BAIXA, MEDIA, ALTA
    }

    public enum Situacao {
        ABERTA, PENDENTE, CONCLUIDA
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @NotBlank(message = "{tarefa.nome.obrigatorio}")
    @Size(max = 100, message = "{tarefa.nome.tamanho}")
    @Column(nullable = false, length = 100, columnDefinition = "TEXT")
    private String nome;

    @Size(max = 500, message = "{tarefa.descricao.tamanho}")
    @Column(length = 500)
    private String descricao;

    @NotNull(message = "{tarefa.prioridade.obrigatoria}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Prioridade prioridade = Prioridade.BAIXA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private Situacao situacao = Situacao.ABERTA;

    @NotNull(message = "{tarefa.data.obrigatoria}")
    @FutureOrPresent(message = "{tarefa.data.invalida}")
    @Column(nullable = false)
    private LocalDate dataPrevistaConclusao;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dataCriacao;
}