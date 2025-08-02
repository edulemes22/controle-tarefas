package com.example.controletarefas.service;

import com.example.controletarefas.dto.TarefaDto;
import com.example.controletarefas.dto.TarefaMapper;
import com.example.controletarefas.exception.TarefaNaoEncontradaException;
import com.example.controletarefas.model.Tarefa;
import com.example.controletarefas.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.example.controletarefas.model.Tarefa.Prioridade;
import static com.example.controletarefas.model.Tarefa.Situacao;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaMapper tarefaMapper;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    void criar_DeveSalvarTarefaERetornarDto() {
        TarefaDto.Criacao dto = new TarefaDto.Criacao();
        dto.setNome("Teste");
        dto.setPrioridade(Prioridade.MEDIA);
        dto.setDataPrevistaConclusao(LocalDate.now().plusDays(1));

        Tarefa tarefaSalva = new Tarefa();
        tarefaSalva.setId(UUID.randomUUID());

        TarefaDto.Resposta respostaEsperada = new TarefaDto.Resposta();
        respostaEsperada.setId(tarefaSalva.getId());

        when(tarefaMapper.toEntity(dto)).thenReturn(new Tarefa());
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaSalva);
        when(tarefaMapper.toResponse(tarefaSalva)).thenReturn(respostaEsperada);

        TarefaDto.Resposta resposta = tarefaService.criar(dto);

        assertNotNull(resposta);

        assertEquals(tarefaSalva.getId(), resposta.getId());

        verify(tarefaRepository).save(any(Tarefa.class));

    }

    @Test
    void buscarPorId_QuandoExistir_DeveRetornarTarefa() {
        UUID id = UUID.randomUUID();
        Tarefa tarefa = new Tarefa();
        tarefa.setId(id);

        TarefaDto.Resposta respostaEsperada = new TarefaDto.Resposta();
        respostaEsperada.setId(id);

        when(tarefaRepository.findById(id)).thenReturn(Optional.of(tarefa));
        when(tarefaMapper.toResponse(tarefa)).thenReturn(respostaEsperada);

        TarefaDto.Resposta resposta = tarefaService.buscarPorId(id);

        assertNotNull(resposta);
        assertEquals(id, resposta.getId());
    }

    @Test
    void buscarPorId_QuandoNaoExistir_DeveLancarExcecao() {
        UUID id = UUID.randomUUID();
        when(tarefaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaException.class, () -> tarefaService.buscarPorId(id));
    }

    @Test
    void marcarComoConcluida_DeveAtualizarSituacao() {
        UUID id = UUID.randomUUID();
        Tarefa tarefa = new Tarefa();
        tarefa.setId(id);
        tarefa.setSituacao(Situacao.ABERTA);

        when(tarefaRepository.findById(id)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(tarefa)).thenReturn(tarefa);
        when(tarefaMapper.toResponse(tarefa)).thenReturn(new TarefaDto.Resposta());

        TarefaDto.Resposta resposta = tarefaService.marcarComoConcluida(id);

        assertEquals(Situacao.CONCLUIDA, tarefa.getSituacao());
        assertNotNull(resposta);
    }
}
