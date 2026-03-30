package com.dominick.agendadortarefas.business.mapper;

import com.dominick.agendadortarefas.business.dto.TarefasDTO;
import com.dominick.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TarefaUpdateConverter {

    void updataTarefas(TarefasDTO dto, @MappingTarget TarefasEntity entity);
}
