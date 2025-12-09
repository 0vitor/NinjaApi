package dev.java.CadastroNinjas.missions;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<MissionModel, Long> {
    boolean existsByName(String name);
}