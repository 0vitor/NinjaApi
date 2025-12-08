package dev.java.CadastroNinjas.ninja;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NinjaRepository extends JpaRepository<NinjaModel, Long> {

    Optional<NinjaModel> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM NinjaModel n WHERE n.username <> :username")
    void deleteAllExceptUsername(String username);
}
