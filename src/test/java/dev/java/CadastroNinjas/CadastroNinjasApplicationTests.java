package dev.java.CadastroNinjas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // <-- usa application-test.properties
class CadastroNinjasApplicationTests {

    @Test
    void contextLoads() {
    }
}
