package com.dominick.agendadortarefas.business;

import com.dominick.agendadortarefas.business.dto.TarefasDTO;
import com.dominick.agendadortarefas.business.mapper.TarefaUpdateConverter;
import com.dominick.agendadortarefas.business.mapper.TarefasConverter;
import com.dominick.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.dominick.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.dominick.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.dominick.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.dominick.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverter tarefaUpdateConverter;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);

        return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefaConverter.paraListaTarefasDTO(
                tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> BuscarTarefasPorEmail(String token) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        List<TarefasEntity> listTarefas = tarefasRepository.findByEmailUsuario(email);

        return tarefaConverter.paraListaTarefasDTO(listTarefas);
    }

    public void deletaTarefaPorId(String id) {

        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por id, id inexistente " + id,
                    e.getCause());
        }
    }

    public TarefasDTO alteraStatus(StatusNotificacaoEnum staus, String id) {
        try {
            TarefasEntity entity = tarefasRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada " + id));
            entity.setStatusNotificacaoEnum(staus);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa " + e.getCause());
        }
    }

    public TarefasDTO updateTarefas(TarefasDTO dto, String id) {
        try {
            TarefasEntity entity = tarefasRepository.
                    findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada " + id));

            tarefaUpdateConverter.updataTarefas(dto, entity);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa " + e.getCause());
        }


    }
}

