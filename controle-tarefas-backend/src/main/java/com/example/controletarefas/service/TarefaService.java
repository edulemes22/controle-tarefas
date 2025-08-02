package com.example.controletarefas.service;

import com.example.controletarefas.dto.TarefaDto;
import com.example.controletarefas.dto.TarefaMapper;
import com.example.controletarefas.exception.TarefaNaoEncontradaException;
import com.example.controletarefas.model.Tarefa;
import com.example.controletarefas.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaMapper tarefaMapper;

    @Transactional
    public TarefaDto.Resposta criar(TarefaDto.Criacao dto) {
        Tarefa tarefa = tarefaMapper.toEntity(dto);
        return tarefaMapper.toResponse(tarefaRepository.save(tarefa));
    }

    @Transactional(readOnly = true)
    public Page<TarefaDto.Resposta> filtrar(
            String nome,
            Tarefa.Prioridade prioridade,
            Tarefa.Situacao situacao,
            Pageable pageable) {

        return tarefaRepository.filtrarTarefas(
                nome,
                prioridade,
                situacao,
                pageable
        ).map(tarefaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TarefaDto.Resposta buscarPorId(UUID id) {
        return tarefaRepository.findById(id)
                .map(tarefaMapper::toResponse)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }

    @Transactional
    public TarefaDto.Resposta atualizar(UUID id, TarefaDto.Atualizacao dto) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));

        tarefaMapper.toEntity(dto, tarefa);
        return tarefaMapper.toResponse(tarefaRepository.save(tarefa));
    }

    @Transactional
    public void deletar(UUID id) {
        if (!tarefaRepository.existsById(id)) {
            throw new TarefaNaoEncontradaException(id);
        }
        tarefaRepository.deleteById(id);
    }

    @Transactional
    public TarefaDto.Resposta marcarComoConcluida(UUID id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));

        tarefa.setSituacao(Tarefa.Situacao.CONCLUIDA);
        return tarefaMapper.toResponse(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaDto.Resposta marcarComoPendente(UUID id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));

        tarefa.setSituacao(Tarefa.Situacao.PENDENTE);
        return tarefaMapper.toResponse(tarefaRepository.save(tarefa));
    }

}
