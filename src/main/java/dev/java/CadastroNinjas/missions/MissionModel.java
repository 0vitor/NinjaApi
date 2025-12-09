package dev.java.CadastroNinjas.missions;

import dev.java.CadastroNinjas.ninjas.NinjaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_missions")
public class MissionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private MissionDifficulty difficulty;

    @OneToMany(mappedBy = "mission")
    private List<NinjaModel> ninjas;
}
