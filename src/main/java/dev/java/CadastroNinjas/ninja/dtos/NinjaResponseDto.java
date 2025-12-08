package dev.java.CadastroNinjas.ninja.dtos;

import java.time.LocalDate;

public record NinjaResponseDto(
        Long id,
        String name,
        LocalDate birthDate
) {}
