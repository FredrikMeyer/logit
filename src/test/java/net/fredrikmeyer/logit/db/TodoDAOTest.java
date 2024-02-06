package net.fredrikmeyer.logit.db;

import net.fredrikmeyer.logit.Todo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoDAOTest {

    @Test
    void fromDomainTodo() {
        var created = LocalDateTime.of(1989, 2, 13, 14, 9);
        var domainTodo = new Todo("my todo", created, 1L);

        var res = TodoDAO.fromDomainTodo(domainTodo);

        assertEquals(res.getContent(), "my todo");
        assertEquals(res.getCreated(), created);
        assertEquals(res.getId(), 1L);
    }

    @Test
    void toDomain() {
        var created = LocalDateTime.of(1989, 2, 13, 14, 9);
        var daoTodo = new TodoDAO("my second todo", created);

        var res = daoTodo.toDomain();

        assertFalse(res.done);
        assertEquals(res.content, "my second todo");
        assertEquals(res.id, daoTodo.getId());
    }
}