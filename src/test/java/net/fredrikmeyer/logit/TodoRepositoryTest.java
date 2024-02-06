package net.fredrikmeyer.logit;

import net.fredrikmeyer.logit.db.TodoDAORepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class TodoRepositoryTest {

    @Test
    void listTodos() {
        var todoRepo = new TodoRepository(todoDAORepository);

        todoRepo.createTodo(new Todo("Hey"));
        var res = todoRepo.listTodos();

        assertEquals(res.size(), 1);
    }

    @Autowired
    private TodoDAORepository todoDAORepository;

    @Test
    void createTodo() {
        var todoRepo = new TodoRepository(todoDAORepository);
        todoRepo.createTodo(new Todo("Check this", 1L));

        var listOfTodos = todoRepo.listTodos();

        var res = listOfTodos.stream().filter(t -> t.id == 1L).findAny();
        assertTrue(res.isPresent());
    }

    @Test
    void markDone() {
        var todoRepo = new TodoRepository(todoDAORepository);
        todoRepo.createTodo(new Todo("My todo", 1L));

        todoRepo.markDone(1L);

        assertTrue(todoRepo
                .listTodos()
                .stream()
                .filter(t -> Objects.equals(t.id, 1L))
                .findAny()
                .get().done);
    }

    @Test
    void deleteTodo() {
        var todoRepo = new TodoRepository(todoDAORepository);

        var todo = todoRepo.createTodo(new Todo("My todo"));

        todoRepo.deleteTodo(todo.id);

        var res = todoRepo.listTodos().stream().filter(t -> Objects.equals(t.id, todo.id)).findAny();
        assertTrue(res.isEmpty());
    }
}