package net.fredrikmeyer.logit;

import jakarta.persistence.Entity;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Todo {
    public String content;
    public Long id;

    public LocalDateTime created = LocalDateTime.now();
    public boolean done = false;

    public Todo(String content, Long id) {
        this.content = content;
        this.id = id;
    }

    public Todo(String value) {
        this.content = value;
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }

    public Todo(String value, LocalDateTime created, Long id) {
        this.content = value;
        this.created = created;
        this.id = id;
    }

    public void markAsDone() {
        this.done = !this.done;
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
        return done == todo.done && Objects.equals(content, todo.content) && Objects.equals(id,
                todo.id) && Objects.equals(created, todo.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, id, created, done);
    }
}
