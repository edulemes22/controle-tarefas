package com.example.controletarefas.controller;

import com.example.controletarefas.dto.TarefaDto;
import com.example.controletarefas.model.Tarefa;
import com.example.controletarefas.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tarefas")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "API para gerenciamento de tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    @Operation(summary = "Criar uma nova tarefa")
    public ResponseEntity<TarefaDto.Resposta> criar(@RequestBody @Valid TarefaDto.Criacao dto) {

        TarefaDto.Resposta resposta = tarefaService.criar(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resposta.getId())
                .toUri();
        return ResponseEntity.created(location).body(resposta);

    }

    @GetMapping
    @Operation(summary = "Listar tarefas com filtros básicos")
    public ResponseEntity<Page<TarefaDto.Resposta>> listarTarefas(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Tarefa.Prioridade prioridade,
            @RequestParam(required = false) Tarefa.Situacao situacao,
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<TarefaDto.Resposta> resultado = tarefaService.filtrar(
                nome,
                prioridade,
                situacao,
                pageable);

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por ID")
    public ResponseEntity<TarefaDto.Resposta> buscarPorId(@PathVariable UUID id) {

        return ResponseEntity.ok(tarefaService.buscarPorId(id));

    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa")
    public ResponseEntity<TarefaDto.Resposta> atualizar(@PathVariable UUID id, @RequestBody @Valid TarefaDto.Atualizacao dto) {

        return ResponseEntity.ok(tarefaService.atualizar(id, dto));

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tarefa")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {

        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Marcar tarefa como concluída")
    public ResponseEntity<TarefaDto.Resposta> marcarComoConcluida(@PathVariable UUID id) {

        return ResponseEntity.ok(tarefaService.marcarComoConcluida(id));

    }

    @PatchMapping("/{id}/pendente")
    @Operation(summary = "Marcar tarefa como pendente")
    public ResponseEntity<TarefaDto.Resposta> marcarComoPendente(@PathVariable UUID id) {

        return ResponseEntity.ok(tarefaService.marcarComoPendente(id));

    }

}
