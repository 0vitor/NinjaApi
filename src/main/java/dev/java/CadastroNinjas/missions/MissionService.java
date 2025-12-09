package dev.java.CadastroNinjas.missions;

import dev.java.CadastroNinjas.mapper.MissionMapper;
import dev.java.CadastroNinjas.missions.dtos.MissionDto;
import dev.java.CadastroNinjas.missions.dtos.MissionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MissionService {
    private final MissionRepository missionRepository;
    private final MissionMapper missionMapper;

    public MissionResponseDto create(MissionDto dto) {
        if (missionRepository.existsByName(dto.name())) {
         throw new ResponseStatusException(HttpStatus.CONFLICT, "Nome de missão já existe");
        }
        MissionModel mission = missionMapper.toModel(dto);
        return missionMapper.toResponse(missionRepository.save(mission));
    }


}
