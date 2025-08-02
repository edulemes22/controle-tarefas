package com.example.controletarefas.repository;

import com.example.controletarefas.model.Tarefa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static com.example.controletarefas.model.Tarefa.Prioridade;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TarefaRepositoryTest {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Test
    void filtrar_ComNome_DeveRetornarTarefasCorrespondentes() {

        Tarefa tarefa1 = new Tarefa();
        tarefa1.setNome("Estudar Spring Boot");
        tarefa1.setPrioridade(Prioridade.ALTA);
        tarefa1.setDataPrevistaConclusao(LocalDate.now().plusDays(1));
        tarefaRepository.save(tarefa1);

        Tarefa tarefa2 = new Tarefa();
        tarefa2.setNome("Fazer compras");
        tarefa2.setPrioridade(Prioridade.BAIXA);
        tarefa2.setDataPrevistaConclusao(LocalDate.now().plusDays(2));
        tarefaRepository.save(tarefa2);

        Page<Tarefa> resultado = tarefaRepository.filtrarTarefas("spring", null, null, PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
        assertEquals("Estudar Spring Boot", resultado.getContent().get(0).getNome());

    }
}
