package net.fredrikmeyer.logit;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class TodoRepositoryTest {

    @Test
    void listTodos() {
    }

    @Test
    void createTodo() {
        var todoRepo = new TodoRepository();
        todoRepo.createTodo(new Todo("Check this", "1"));

        var listOfTodos = todoRepo.listTodos();

        assertTrue(listOfTodos.contains(new Todo("Check this", "1")));
    }

    @Test
    void markDone() {
        var todoRepo = new TodoRepository();
        todoRepo.createTodo(new Todo("My todo", "1"));

        todoRepo.markDone("1");

        assertTrue(todoRepo
                .listTodos()
                .stream()
                .filter(t -> Objects.equals(t.id, "1"))
                .findAny()
                .get().done);
    }

    @Test
    void deleteTodo() {
        var todoRepo = new TodoRepository();

        var todo = todoRepo.createTodo(new Todo("My todo"));

        todoRepo.deleteTodo(todo.id);

        var res = todoRepo.listTodos().stream().filter(t -> Objects.equals(t.id, todo.id)).findAny();
        assertTrue(res.isEmpty());
    }
}