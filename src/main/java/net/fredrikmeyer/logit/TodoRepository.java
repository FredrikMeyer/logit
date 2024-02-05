package net.fredrikmeyer.logit;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TodoRepository {

    private Map<String, Todo> todos;

    public TodoRepository() {
        var dummys = new ArrayList<Todo>();
        dummys.add(new Todo("Kjøp mat", "1"));
        dummys.add(new Todo("rydd hjemme", "2"));

        this.todos = dummys.stream().collect(Collectors.toMap(t -> t.id, t -> t));
    }

    public List<Todo> listTodos() {
        return this.todos.values().stream().toList();
    }

    public Todo createTodo(Todo todo) {
        this.todos.put(todo.id, todo);

        return todo;
    }

    public boolean deleteTodo(String id) {
        var res = this.todos.remove(id);

        return res != null;
    }

    public Todo markDone(String id) {
        var todo = this.todos.get(id);
        if (todo == null) {
            throw new RuntimeException("Could not find todo " + id);
        }

        todo.done = !todo.done;

        return todo;
    }
}
