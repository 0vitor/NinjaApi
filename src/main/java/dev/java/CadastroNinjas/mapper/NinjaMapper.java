package dev.java.CadastroNinjas.mapper;

import dev.java.CadastroNinjas.ninjas.dtos.NinjaResponseDto;
import dev.java.CadastroNinjas.ninjas.dtos.NinjaUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import dev.java.CadastroNinjas.ninjas.dtos.NinjaDto;
import dev.java.CadastroNinjas.ninjas.NinjaModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface NinjaMapper {
    NinjaModel toModel(NinjaDto dto);
    NinjaResponseDto toResponse(NinjaModel model);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(@MappingTarget NinjaModel target, NinjaUpdateDto dto);
}
