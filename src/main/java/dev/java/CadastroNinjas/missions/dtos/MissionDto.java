package dev.java.CadastroNinjas.missions.dtos;


import dev.java.CadastroNinjas.missions.MissionDifficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MissionDto (
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 70)
        String name,

        @NotBlank(message = "Description is required")
        String description,

        MissionDifficulty difficulty
)
{}
