package dev.java.CadastroNinjas.ninja.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record NinjaUpdateDto(

        @Size(min = 2, max = 40, message = "Name must be between 2 and 40 characters")
        @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only letters")
        String name,

        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
        LocalDate birthDate,

        @Size(min = 4, max = 30)
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username contains invalid characters")
        String username
) {}
