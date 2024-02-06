package net.fredrikmeyer.logit;

import net.fredrikmeyer.logit.db.TodoDAO;
import net.fredrikmeyer.logit.db.TodoDAORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TodoRepository {

    Logger logger = LoggerFactory.getLogger(TodoRepository.class);
    private final TodoDAORepository todoDAORepository;

    public TodoRepository(TodoDAORepository todoDAORepository) {
        this.todoDAORepository = todoDAORepository;

    }

    public List<Todo> listTodos() {
        return this.todoDAORepository.findAll()
                .stream()
                .map(TodoDAO::toDomain)
                .toList();
    }

    public Todo createTodo(Todo todo) {
        var res = this.todoDAORepository.save(TodoDAO.fromDomainTodo(todo));
        return res.toDomain();
    }

    public void deleteTodo(Long id) {
        try {
            this.todoDAORepository.deleteById(id);
        } catch (RuntimeException ignored) {
        }
    }

    public long numberOfDone() {
        return this.todoDAORepository.countByDoneIsTrue();
    }

    public long numberOfTodos() {
        return this.todoDAORepository.count();
    }

    public Todo markDone(Long id) {
        var todo = this.todoDAORepository.findById(id);
        var domainTodo = todo.map(TodoDAO::toDomain)
                .orElseThrow();
        domainTodo.markAsDone();

        logger.info("test" + this.todoDAORepository.countByDoneIsTrue());
        this.todoDAORepository.setDone(id, domainTodo.done);

        return domainTodo;
    }
}
