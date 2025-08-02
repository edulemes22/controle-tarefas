package com.example.controletarefas.dto;

import com.example.controletarefas.model.Tarefa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TarefaMapper {
    TarefaMapper INSTANCE = Mappers.getMapper(TarefaMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "situacao", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    Tarefa toEntity(TarefaDto.Criacao dto);

    @Mapping(target = "situacao", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    Tarefa toEntity(TarefaDto.Atualizacao dto, @org.mapstruct.MappingTarget Tarefa entity);

    TarefaDto.Resposta toResponse(Tarefa entity);
}
