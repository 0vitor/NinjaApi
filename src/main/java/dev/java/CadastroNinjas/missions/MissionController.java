package dev.java.CadastroNinjas.missions;
import dev.java.CadastroNinjas.missions.dtos.MissionDto;
import dev.java.CadastroNinjas.missions.dtos.MissionResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MissionController {
    private final MissionService missionService;

    @PostMapping("/mission")
    public ResponseEntity<MissionResponseDto> createMission(@RequestBody @Valid MissionDto dto) {
        MissionResponseDto mission = missionService.create(dto);
        URI location = URI.create("/api/mission/" + mission.id());
        return ResponseEntity.created(location).body(mission);
    }
}
