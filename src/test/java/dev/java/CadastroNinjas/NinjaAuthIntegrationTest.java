package dev.java.CadastroNinjas;

import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

public class NinjaAuthIntegrationTest extends BaseIntegrationTest{

    @Test
    void testLogin() throws Exception {
        mvc.perform(formLogin().user("usertest").password("123456")).andExpect(authenticated());
    }

    @Test
    void testFailLogin() throws Exception {
        mvc.perform(formLogin().user("usertest").password("invalidPass")).andExpect(unauthenticated());
    }
}

