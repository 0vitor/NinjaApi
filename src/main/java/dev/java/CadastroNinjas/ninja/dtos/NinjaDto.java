package dev.java.CadastroNinjas.ninja.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record NinjaDto(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 40)
        @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only letters")
        String name,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
        LocalDate birthDate,

        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 30)
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username contains invalid characters")
        String username
) {}
