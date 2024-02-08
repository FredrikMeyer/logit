package net.fredrikmeyer.logit.site;

import net.fredrikmeyer.logit.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SiteIntegrationTest {
    @Autowired
    TodoRepository todoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;


    @Test
    @WithMockUser(username = "user", password = "password")
    void newTodo() throws Exception {
        this.mockMvc.perform(post("/todo").with(csrf())
                        .param("value", "my new todo"
                        )
                        .param("deadline", ""))
                .andExpect(status().isOk());

        var allTodos = todoRepository.listTodos();
        assertEquals(allTodos.size(), 1);
        var firstTodo = allTodos.getFirst();
        assertEquals(firstTodo.content, "my new todo");
    }

    @Test
    @WithMockUser(username = "user", password = "password")
    void newTodoWithDeadline() throws Exception {
        this.mockMvc.perform(post("/todo").with(csrf())
                        .param("value", "my new todo"
                        )
                        .param("deadline", "2023-12-11"))
                .andExpect(status().isOk());

        var allTodos = todoRepository.listTodos();
        assertEquals(allTodos.size(), 1);
        var firstTodo = allTodos.getFirst();
        assertEquals(firstTodo.content, "my new todo");
        assertEquals(firstTodo.deadline, LocalDateTime.of(2023, 12,11, 0, 0));
    }
}