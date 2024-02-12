package net.fredrikmeyer.logit.controllers;

import net.fredrikmeyer.logit.TestRedisConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllersTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void redirectsOnSuccessfulLogin() throws Exception {
        this.mockMvc.perform(post("/login").with(csrf())
                        .param("username", "user"
                        )
                        .param("password", "p"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andReturn();
    }

    @Test
    void addsErrorUrlWithWrongLogin() throws Exception {
        this.mockMvc.perform(post("/login").with(csrf())
                        .param("username", "userxxx"
                        )
                        .param("password", "passwordxxx"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void isRedirectedToLoginPageOnLoada() throws Exception {
        this.mockMvc.perform(get("/").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void loadsLoginForm() throws Exception {
        this.mockMvc.perform(get("/login").with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(redirectedUrl(null))
                .andExpect(content().string(containsString(
                        "input name=\"username\" id=\"username\" autocomplete=\"username\">")))
                .andReturn();
    }
}