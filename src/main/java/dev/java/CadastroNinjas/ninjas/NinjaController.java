package dev.java.CadastroNinjas.ninjas;

import dev.java.CadastroNinjas.ninjas.dtos.NinjaDto;
import dev.java.CadastroNinjas.ninjas.dtos.NinjaResponseDto;
import dev.java.CadastroNinjas.ninjas.dtos.NinjaUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NinjaController {
    private final NinjaService ninjaService;

    @GetMapping("/ninjas")
    public ResponseEntity<List<NinjaResponseDto>> getNinjas() {
        List<NinjaResponseDto> ninjas = ninjaService.findAll();
        return ResponseEntity.ok(ninjas);
    }

    @GetMapping("/ninja/{id}")
    public ResponseEntity<NinjaResponseDto> getNinjaById(@PathVariable Long id) {
        Optional<NinjaResponseDto> optionalNinja = ninjaService.findById(id);
        return optionalNinja.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/ninjas")
    public ResponseEntity<Void> deleteNinjas() {
        ninjaService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<NinjaResponseDto> createNinja(@RequestBody @Valid NinjaDto dto) {
        NinjaResponseDto ninja = ninjaService.create(dto);
        URI location = URI.create("/api/ninja/" + ninja.id());
        return ResponseEntity.created(location).body(ninja);
    }

    @PatchMapping("/ninja/{id}")
    public ResponseEntity<NinjaResponseDto> updateNinja(
            @PathVariable Long id,
            @RequestBody @Valid NinjaUpdateDto dto
    ) {
        Optional<NinjaResponseDto> updated = ninjaService.update(id, dto);

        return updated
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
