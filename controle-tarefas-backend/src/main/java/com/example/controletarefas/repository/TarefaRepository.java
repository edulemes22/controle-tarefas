package com.example.controletarefas.repository;

import com.example.controletarefas.model.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, UUID> {

    @Query("SELECT t FROM Tarefa t WHERE " +
            "(COALESCE(:nome, '') = '' OR LOWER(t.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:prioridade IS NULL OR t.prioridade = :prioridade) " +
            "AND (:situacao IS NULL OR t.situacao = :situacao)")
    Page<Tarefa> filtrarTarefas(
            @Param("nome") String nome,
            @Param("prioridade") Tarefa.Prioridade prioridade,
            @Param("situacao") Tarefa.Situacao situacao,
            Pageable pageable);
}
