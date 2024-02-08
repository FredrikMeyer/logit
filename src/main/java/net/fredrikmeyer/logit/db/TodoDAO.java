package net.fredrikmeyer.logit.db;

import jakarta.persistence.*;
import net.fredrikmeyer.logit.Todo;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "todos")
public class TodoDAO {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
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

    @Column()
    public LocalDateTime deadline;

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
        toReturn.deadline = todo.deadline;
        return toReturn;
    }

    public Todo toDomain() {
        var toReturn = new Todo.TodoBuilder().withContent(this.content)
                .withCreated(this.created)
                .withId(this.id)
                .withDeadLine(this.deadline)
                .build();
        toReturn.done = this.done;
        toReturn.deadline = this.deadline;
        return toReturn;
    }
}
