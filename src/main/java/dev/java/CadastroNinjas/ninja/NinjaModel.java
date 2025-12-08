package dev.java.CadastroNinjas.ninja;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_ninjas")
public class NinjaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(columnDefinition = "text[]")
    List<String> role;

    @Column(nullable = false)
    LocalDate birthDate;

    @Column(nullable = false)
    String password;

}
