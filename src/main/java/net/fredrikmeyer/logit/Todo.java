package net.fredrikmeyer.logit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Todo {
    public String content;
    public Long id;

    public LocalDateTime deadline = null;
    public LocalDateTime created = LocalDateTime.now();
    public boolean done = false;

    public Todo() {

    }

    public void markAsDone() {
        this.done = !this.done;
    }

    public String humanString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        var dateString = this.created.format(formatter);
        return this.content + " (" + dateString + ")" + (this.deadline != null ? " Deadline: " + this.deadline : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return done == todo.done && Objects.equals(content, todo.content) && Objects.equals(id,
                todo.id) && Objects.equals(deadline, todo.deadline) && Objects.equals(created,
                todo.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, id, deadline, created, done);
    }

    public static class TodoBuilder {
        private final Todo todo = new Todo();

        public TodoBuilder() {
        }

        public TodoBuilder withContent(String content) {
            this.todo.content = content;
            return this;
        }

        public TodoBuilder withId(Long id) {
            this.todo.id = id;
            return this;
        }

        public TodoBuilder withDeadLine(LocalDateTime deadline) {
            this.todo.deadline = deadline;
            return this;
        }

        public TodoBuilder withCreated(LocalDateTime created) {
            this.todo.created = created;
            return this;
        }

        public Todo build() {
            return this.todo;
        }
    }
}
