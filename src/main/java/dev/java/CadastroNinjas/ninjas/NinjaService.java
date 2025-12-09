package dev.java.CadastroNinjas.ninjas;

import dev.java.CadastroNinjas.mapper.NinjaMapper;
import dev.java.CadastroNinjas.ninjas.dtos.NinjaDto;
import dev.java.CadastroNinjas.ninjas.dtos.NinjaResponseDto;
import dev.java.CadastroNinjas.ninjas.dtos.NinjaUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class NinjaService {
    private final NinjaRepository ninjaRepository;
    private final PasswordEncoder passwordEncoder;
    private final NinjaMapper mapper;

    public NinjaService(NinjaRepository ninjaRepository, PasswordEncoder passwordEncoder,NinjaMapper mapper ) {
        this.ninjaRepository = ninjaRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    public List<NinjaResponseDto> findAll() {
        return ninjaRepository.findAll()
                              .stream()
                              .map(mapper::toResponse)
                              .toList();
    }

    public Optional<NinjaResponseDto> findById(Long id) {
        return ninjaRepository.findById(id).map(mapper::toResponse);
    }

    public Optional<NinjaResponseDto> update(Long id, NinjaUpdateDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        NinjaModel ninja = ninjaRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        assert auth != null;
        String loggedUsername = auth.getName();
        boolean isSameUser = ninja.getUsername().equals(loggedUsername);
        boolean isAdmin = auth.getAuthorities().toString().contains("ADMIN");

        if (!isAdmin && !isSameUser) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode atualizar outro usuário!");
        }

        mapper.updateModel(ninja, dto);
        ninjaRepository.save(ninja);
        return Optional.of(mapper.toResponse(ninja));
    }

    public NinjaResponseDto create(NinjaDto dto) {
        if (ninjaRepository.existsByUsername(dto.username())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Nome de usuário já existe"
            );
        }
        NinjaModel model = mapper.toModel(dto);
        model.setRole(List.of("USER"));
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        return mapper.toResponse(ninjaRepository.save(model));
    }

    public void deleteAll() {
        ninjaRepository.deleteAll();
    }

    public void deleteAllExceptUserTest() {
        ninjaRepository.deleteAllExceptUsername("usertest");
    }

    public Optional<NinjaModel> findByUsername(String username) {
        return ninjaRepository.findByUsername(username);
    }
}
