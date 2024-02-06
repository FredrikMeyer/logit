package net.fredrikmeyer.logit.db;

import jakarta.persistence.*;
import net.fredrikmeyer.logit.Todo;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
public class TodoDAO {
    @Id
    @GeneratedValue
    private long id;

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public boolean isDone() {
        return done;
    }

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "done", nullable = false, columnDefinition = "boolean default true")
    public boolean done = false;

    public TodoDAO() {
    }

    public TodoDAO(String content, LocalDateTime created) {
        this.content = content;
        this.created = created;
    }

    public static TodoDAO fromDomainTodo(Todo todo) {
        var toReturn = new TodoDAO(todo.content, todo.created);
        toReturn.done = todo.done;
        toReturn.id = todo.id;
        return toReturn;
    }

    public Todo toDomain() {
        var toReturn = new Todo(this.content, this.created, this.id);
        toReturn.done = this.done;
        return toReturn;
    }
}
