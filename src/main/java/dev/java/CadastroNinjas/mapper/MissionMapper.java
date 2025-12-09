package dev.java.CadastroNinjas.mapper;

import dev.java.CadastroNinjas.missions.MissionModel;
import dev.java.CadastroNinjas.missions.dtos.MissionDto;
import dev.java.CadastroNinjas.missions.dtos.MissionResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MissionMapper {
    MissionModel toModel(MissionDto dto);
    MissionResponseDto toResponse(MissionModel model);
}
