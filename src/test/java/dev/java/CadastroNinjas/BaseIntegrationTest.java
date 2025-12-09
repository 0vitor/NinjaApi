package dev.java.CadastroNinjas;

import dev.java.CadastroNinjas.ninjas.NinjaRepository;
import dev.java.CadastroNinjas.ninjas.NinjaService;
import dev.java.CadastroNinjas.ninjas.dtos.NinjaDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("test") // use application-test.properties
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public abstract class BaseIntegrationTest {

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected NinjaRepository ninjaRepository;

    @Autowired
    protected NinjaService ninjaService;

    @Autowired
    ObjectMapper objectMapper;

    protected MockMvc mvc;

    @BeforeAll
    void globalSetup() {
        ninjaRepository.deleteAll();

        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        NinjaDto ninja = new NinjaDto(
                "usertest",
                "123456",
                LocalDate.of(2002, 6, 22),
                "usertest"
        );
        ninjaService.create(ninja);
    }

    @BeforeEach
    void clearUp() {
        ninjaService.deleteAllExceptUserTest();
    }
}