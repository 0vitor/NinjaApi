package dev.java.CadastroNinjas.security;

import dev.java.CadastroNinjas.ninja.NinjaModel;
import dev.java.CadastroNinjas.ninja.NinjaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NinjaDetailsService implements UserDetailsService {
    private final NinjaRepository ninjaRepository;

    public NinjaDetailsService(NinjaRepository ninjaRepository) {
        this.ninjaRepository = ninjaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NinjaModel ninja = ninjaRepository.findByUsername(username)
                                          .orElseThrow(() -> new UsernameNotFoundException("Ninja not found: " + username));
        String[] roles = ninja.getRole() != null
                ? ninja.getRole().toArray(new String[0])
                : new String[]{};

        return User.builder()
                   .username(ninja.getUsername())
                   .password(ninja.getPassword())
                   .roles(roles)
                   .build();
    }
}
