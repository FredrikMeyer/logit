package net.fredrikmeyer.logit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Todo {
    public String content;
    public String id;

    public LocalDateTime created = LocalDateTime.now();
    public boolean done = false;

    public Todo(String content, String id) {
        this.content = content;
        this.id = id;
    }

    public Todo(String value) {
        this.content = value;
        this.id = UUID.randomUUID().toString();
    }

    public String humanString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        var dateString = this.created.format(formatter);
        return this.content + " (" + dateString + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(content, todo.content) && Objects.equals(id,
                todo.id) && Objects.equals(created, todo.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, id, created);
    }
}
