package com.dominick.agendadortarefas.business.mapper;

import com.dominick.agendadortarefas.business.dto.TarefasDTO;
import com.dominick.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarefasConverter {

    TarefasEntity paraTarefaEntity(TarefasDTO dto);

    TarefasDTO paraTarefaDTO(TarefasEntity entity);
}
