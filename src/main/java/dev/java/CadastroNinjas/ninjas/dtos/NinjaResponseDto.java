package dev.java.CadastroNinjas.ninjas.dtos;

import dev.java.CadastroNinjas.missions.dtos.MissionResponseDto;

import java.time.LocalDate;

public record NinjaResponseDto(
        Long id,
        String name,
        LocalDate birthDate,
        MissionResponseDto mission
) {}
