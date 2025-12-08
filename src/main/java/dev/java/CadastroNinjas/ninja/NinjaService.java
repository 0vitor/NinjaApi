package dev.java.CadastroNinjas.ninja;

import dev.java.CadastroNinjas.mapper.NinjaMapper;
import dev.java.CadastroNinjas.ninja.dtos.NinjaDto;
import dev.java.CadastroNinjas.ninja.dtos.NinjaResponseDto;
import dev.java.CadastroNinjas.ninja.dtos.NinjaUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NinjaService {
    private final NinjaRepository ninjaRepository;
    private  final PasswordEncoder passwordEncoder;

    @Autowired
    private NinjaMapper mapper;

    public NinjaService(NinjaRepository ninjaRepository, PasswordEncoder passwordEncoder) {
        this.ninjaRepository = ninjaRepository;
        this.passwordEncoder = passwordEncoder;
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

        String username = SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getName();

        NinjaModel loggedUser = ninjaRepository.findByUsername(username)
                                               .orElseThrow();

        boolean isAdmin = loggedUser.getRole().contains("ADMIN");

        if (!isAdmin && !loggedUser.getId().equals(id)) {
            throw new RuntimeException("Você não pode atualizar outro usuário!");
        }

        return ninjaRepository.findById(id).map(ninja -> {
            mapper.updateModel(ninja, dto);
            ninjaRepository.save(ninja);
            return mapper.toResponse(ninja);
        });
    }

    public NinjaResponseDto create(NinjaDto dto) {
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
