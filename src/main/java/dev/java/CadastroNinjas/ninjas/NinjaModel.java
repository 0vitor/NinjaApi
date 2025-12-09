package dev.java.CadastroNinjas.ninjas;

import dev.java.CadastroNinjas.missions.MissionModel;
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
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(columnDefinition = "text[]")
    private List<String> role;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private MissionModel mission;
}
