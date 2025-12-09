package dev.java.CadastroNinjas.missions.dtos;

import dev.java.CadastroNinjas.missions.MissionDifficulty;

public record MissionResponseDto (
        Long id,
        String name,
        String description,
        MissionDifficulty difficulty
){
}
