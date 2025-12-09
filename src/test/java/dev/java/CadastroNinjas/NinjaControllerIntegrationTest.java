package dev.java.CadastroNinjas;

import dev.java.CadastroNinjas.ninjas.dtos.NinjaDto;
import dev.java.CadastroNinjas.ninjas.dtos.NinjaUpdateDto;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NinjaControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser(username = "Itachi1", roles = "ADMIN")
    @NotNull(message = "Mission ID is required")
    void shouldAllowAdminToUpdateOtherUser() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Itachi Uchiha",
                "092438",
                LocalDate.of(1992, 3, 10),
                "Itachi1",
                null
        );

        NinjaDto ninja2 = new NinjaDto(
                "Itachi Uchiha",
                "092438",
                LocalDate.of(1992, 3, 10),
                "Itachi2",
                null
        );

        ninjaService.create(ninja);
        Long ninja2Id = ninjaService.create(ninja2).id();

        NinjaUpdateDto updateDto = new NinjaUpdateDto(
                "Sasuke Uchiha",
                null,
                null,
                null
        );

        String body = objectMapper.writeValueAsString(updateDto);

        mvc.perform(patch("/api/ninja/" + ninja2Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
           .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "Itachi1", roles = "USER")
    void shouldUpdateNinjaSuccessfully() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Itachi Uchiha",
                "092438",
                LocalDate.of(1992, 3, 10),
                "Itachi1",
                null
        );
        Long id = ninjaService.create(ninja).id();

        NinjaUpdateDto updateDto = new NinjaUpdateDto(
                "Sasuke Uchiha",
                null,
                null,
                null
        );

        String body = objectMapper.writeValueAsString(updateDto);

        mvc.perform(patch("/api/ninja/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Sasuke Uchiha"))
               .andExpect(jsonPath("$.birthDate").value("1992-03-10"));
    }

    @Test
    @WithMockUser(username = "Itachi1", roles = "USER")
    void shouldForbidUserFromUpdatingAnotherUser() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Itachi Uchiha",
                "092438",
                LocalDate.of(1992, 3, 10),
                "Itachi1",
                null
        );
        ninjaService.create(ninja);
        Long usertestId = ninjaService.findByUsername("usertest")
                                      .orElseThrow(() -> new IllegalStateException("Usuário 'usertest' não existe no banco de testes"))
                                      .getId();


        NinjaUpdateDto updateDto = new NinjaUpdateDto(
                "Sasuke Uchiha",
                null,
                null,
                null
        );

        String body = objectMapper.writeValueAsString(updateDto);

        mvc.perform(patch("/api/ninja/" + usertestId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "usertest", roles = "USER")
    void shouldReturnNotFoundWhenUpdatingNonexistentUser() throws Exception {
        NinjaUpdateDto updateDto = new NinjaUpdateDto(
                "Novo Nome",
                null,
                null,
                null
        );
        String body = objectMapper.writeValueAsString(updateDto);
        mvc.perform(patch("/api/ninja/999999") // id inexistente
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(body))
               .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "usertest", roles = "USER")
    void shouldReturnOneNinja() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Itachi Uchiha",
                "092438",
                LocalDate.of(1992, 3, 10),
                "Itachi1",
                null
        );
        Long id = ninjaService.create(ninja).id();

        mvc.perform(get("/api/ninja/" + id).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value(ninja.name()));
    }

    @Test
    @WithMockUser(username = "usertest", roles = "USER")
    void shouldReturnNotFoundNinja() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Itachi Uchiha",
                "092438",
                LocalDate.of(1992, 3, 10),
                "Itachi1",
                null
        );
        ninjaService.create(ninja);

        mvc.perform(get("/api/ninja/2").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "usertest", roles = "USER")
    void shouldReturnListWithOneNinja() throws Exception {

        mvc.perform(get("/api/ninjas")
                                .contentType(MediaType.APPLICATION_JSON)
                                )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "usertest", roles = "USER")
    void shouldReturnListOfNinjas() throws Exception {
        ninjaService.create(new NinjaDto(
                "Sasuke Uchiha",
                "abcdef",
                LocalDate.of(1992, 7, 23),
                "Sasuke1",
                null
        ));

        ninjaService.create(new NinjaDto(
                "Sakura Haruno",
                "123456",
                LocalDate.of(1993, 3, 28),
                "Sakura1",
                null
        ));

        int usertest = 1;
        mvc.perform(get("/api/ninjas").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(2 + usertest));
    }

    @Test
    @WithMockUser(username = "usertest", roles = "USER")
    void shouldCreateNinjaSuccessfully() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Naruto Uzumaki",
                "123456",
                LocalDate.of(1990, 10, 10),
                "Naruto1",
                null
        );

        mvc.perform(post("/api/signup/ninja")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ninja)))
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"))
               .andExpect(jsonPath("$.name").value(ninja.name()))
               .andExpect(jsonPath("$.birthDate").value(ninja.birthDate().toString()))
               .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "usertest", roles = "USER")
    void shouldNotCreateIfUsernameAlreadyExist() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "Naruto Uzumaki",
                "123456",
                LocalDate.of(1990, 10, 10),
                "usertest",
                null
        );

        mvc.perform(post("/api/signup/ninja")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(ninja)))
           .andExpect(status().isConflict());
    }


    @Test
    @WithMockUser(username = "usertest", roles = "USER")
    void shouldReturnValidationErrorForInvalidNinja() throws Exception {
        NinjaDto ninja = new NinjaDto(
                "",
                "",
                LocalDate.of(2000, 1, 1),
                "",
                null
        );

        mvc.perform(post("/api/signup/ninja")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ninja)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errors").exists());
    }
}
