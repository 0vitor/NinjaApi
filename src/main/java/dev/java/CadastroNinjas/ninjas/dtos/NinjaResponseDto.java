package dev.java.CadastroNinjas.ninjas.dtos;

import java.time.LocalDate;

public record NinjaResponseDto(
        Long id,
        String name,
        LocalDate birthDate
) {}
