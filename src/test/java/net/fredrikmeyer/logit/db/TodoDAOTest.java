package net.fredrikmeyer.logit.db;

import net.fredrikmeyer.logit.Todo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TodoDAOTest {

    @Test
    void fromDomainTodo() {
        var created = LocalDateTime.of(1989, 2, 13, 14, 9);
        var domainTodo = new Todo.TodoBuilder().withContent("my todo")
                .withId(1L)
                .withCreated(created)
                .build();
        LocalDateTime deadline = LocalDateTime.of(2022, 2, 13, 14, 10);
        domainTodo.deadline = deadline;

        var res = TodoDAO.fromDomainTodo(domainTodo);

        assertEquals(res.getContent(), "my todo");
        assertEquals(res.getCreated(), created);
        assertEquals(res.getId(), 1L);
        assertEquals(res.deadline, deadline);
    }

    @Test
    void toDomain() {
        var created = LocalDateTime.of(1989, 2, 13, 14, 9);
        var daoTodo = new TodoDAO("my second todo", created);
        LocalDateTime deadline = LocalDateTime.of(2022, 2, 13, 14, 10);
        daoTodo.deadline = deadline;

        var res = daoTodo.toDomain();

        assertFalse(res.done);
        assertEquals(res.content, "my second todo");
        assertEquals(res.id, daoTodo.getId());
        assertEquals(res.deadline, deadline);
    }
}