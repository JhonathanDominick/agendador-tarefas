package com.dominick.agendadortarefas.business.mapper;

import com.dominick.agendadortarefas.business.dto.TarefasDTO;
import com.dominick.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.dominick.agendadortarefas.infrastructure.repository.TarefasRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TarefasConverter {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "dataEvento", target = "dataEvento")
    @Mapping(source = "dataCriacao", target = "dataCriacao")

    TarefasEntity paraTarefaEntity(TarefasDTO dto);

    TarefasDTO paraTarefaDTO(TarefasEntity entity);

   List<TarefasEntity> paraListaTarefasEntity(List<TarefasDTO> dtos);

   List<TarefasDTO> paraListaTarefasDTO(List<TarefasEntity> entity);
}
