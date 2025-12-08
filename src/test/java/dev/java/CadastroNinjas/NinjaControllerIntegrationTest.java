package dev.java.CadastroNinjas;

import dev.java.CadastroNinjas.ninja.dtos.NinjaDto;
import dev.java.CadastroNinjas.ninja.NinjaService;
import dev.java.CadastroNinjas.ninja.dtos.NinjaUpdateDto;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test") // usa application-test.properties
class NinjaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NinjaService ninjaService;

    private String sessionId;

    @BeforeAll
    void setup() throws Exception {
        String username = "usertest";
        String password = "123456";

        // Cria o usuário apenas se não existir
        if (ninjaService.findByUsername(username).isEmpty()) {
            MvcResult signupResult = mockMvc.perform(post("/signup/ninja")
                                                             .contentType(MediaType.APPLICATION_JSON)
                                                             .content(objectMapper.writeValueAsString(new NinjaDto(
                                                                     "Naruto Uzumaki",
                                                                     "123456",
                                                                     LocalDate.of(1990, 10, 10),
                                                                     "usertest"
                                                             ))))
                                            .andReturn();

        }
    }

    @BeforeEach
    void clearUp() {
        ninjaService.deleteAllExceptUserTest();
    }


    @Test
    void performFailLogin() throws Exception {
        mockMvc.perform(formLogin().password("invalid"))
                .andExpect(unauthenticated());
    }

    @Test
    void performLogin() throws Exception {
        mockMvc.perform(formLogin().user("usertest").password("123456"))
               .andExpect(authenticated());
    }

    @Test
    @WithMockUser(username = "usertest")
    void testAdminEndpointWithAdminRole() throws Exception {
        mockMvc.perform(get("/ninjas"))
               .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateNinjaSuccessfully() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Itachi Uchiha",
                "092438",
                LocalDate.of(1992, 3, 10),
                "Itachi1"
        );
        Long id = ninjaService.create(ninja).id();

        NinjaUpdateDto updateDto = new NinjaUpdateDto(
                "Sasuke Uchiha",
                null,
                null
        );

        String body = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(patch("/api/ninja/" + id)
                                .contentType(MediaType.APPLICATION_JSON).header("Cookie", sessionId)
                                .content(body).header("Cookie", sessionId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Sasuke Uchiha"))
               .andExpect(jsonPath("$.birthDate").value("1992-03-10"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingNinja() throws Exception {
        NinjaUpdateDto updateDto = new NinjaUpdateDto(
                "Novo Nome",
                null,
                null
        );

        String body = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(patch("/api/ninja/999999") // id inexistente
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .header("Cookie", sessionId)
                                                   .content(body).header("Cookie", sessionId))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOneNinja() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Itachi Uchiha",
                "092438",
                LocalDate.of(1992, 3, 10),
                "Itachi1"
        );
        Long id = ninjaService.create(ninja).id();

        mockMvc.perform(get("/api/ninja/" + id).contentType(MediaType.APPLICATION_JSON)
                                               .header("Cookie", sessionId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value(ninja.name()));
    }

    @Test
    void shouldReturnNotFoundNinja() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Itachi Uchiha",
                "092438",
                LocalDate.of(1992, 3, 10),
                "Itachi1"
        );
        ninjaService.create(ninja);

        mockMvc.perform(get("/api/ninja/2").contentType(MediaType.APPLICATION_JSON).header("Cookie", sessionId))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnEmptyListWhenNoNinjasExist() throws Exception {

        mockMvc.perform(get("/api/ninjas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Cookie", sessionId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(0)); // lista vazia
    }

    @Test
    void shouldReturnListOfNinjas() throws Exception {
        ninjaService.create(new NinjaDto(
                "Sasuke Uchiha",
                "abcdef",
                LocalDate.of(1992, 7, 23),
                "Sasuke1"
        ));

        ninjaService.create(new NinjaDto(
                "Sakura Haruno",
                "123456",
                LocalDate.of(1993, 3, 28),
                "Sakura1"
        ));

        int usertest = 1;
        mockMvc.perform(get("/api/ninjas").contentType(MediaType.APPLICATION_JSON).header("Cookie", sessionId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(2 + usertest));
    }

    @Test
    void shouldCreateNinjaSuccessfully() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Naruto Uzumaki",
                "123456",
                LocalDate.of(1990, 10, 10),
                "Naruto1"
        );

        mockMvc.perform(post("/api/ninja")
                                .contentType(MediaType.APPLICATION_JSON).header("Cookie", sessionId)
                                .content(objectMapper.writeValueAsString(ninja)))
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"))
               .andExpect(jsonPath("$.name").value(ninja.name()))
               .andExpect(jsonPath("$.birthDate").value(ninja.birthDate().toString()))
               .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldReturnValidationErrorForInvalidNinja() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "",
                "",
                LocalDate.of(2000, 1, 1),
                ""
        );

        mockMvc.perform(post("/api/ninja").contentType(MediaType.APPLICATION_JSON).header("Cookie", sessionId)
                                          .content(objectMapper.writeValueAsString(ninja)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors").exists());
    }
}
