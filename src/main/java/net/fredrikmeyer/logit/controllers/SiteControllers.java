package net.fredrikmeyer.logit.controllers;

import jakarta.servlet.http.HttpServletRequest;
import net.fredrikmeyer.logit.Todo;
import net.fredrikmeyer.logit.TodoRepository;
import net.fredrikmeyer.logit.site.Site;
import net.fredrikmeyer.logit.site.TodoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import static net.fredrikmeyer.logit.controllers.Helpers.getLocalDateTime;

@RestController()
public class SiteControllers {
    static Logger logger = LoggerFactory.getLogger(SiteControllers.class);
    private final Site site;
    private final TodoRepository todoRepository;

    public SiteControllers(Site site, TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
        this.site = site;
    }

    @GetMapping("/")
    public String root(HttpServletRequest request) {
        String csrfToken = Helpers.extractCsrfToken(request);
        return this.site.root(csrfToken);
    }

    @RequestMapping("/todos")
    public String todos() {
        var todos = this.todoRepository.listTodos();
        var numberDone = this.todoRepository.numberOfDone();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this.site.getTodos(todos, numberDone);
    }

    @RequestMapping("/todos/expanded/{id}")
    public String todos(@PathVariable long id) {
        var todo = this.todoRepository.getTodo(id);
        TodoView todoView = new TodoView();

        return todoView.todoExpanded(todo);
    }

    @GetMapping("/todos/summary")
    public String todoSummary() {
        var total = this.todoRepository.numberOfTodos();
        var done = this.todoRepository.numberOfDone();
        return this.site.todoSummaryUpdated(total, done);
    }

    @PostMapping("/todo")
    public ResponseEntity<String> newTodo(HttpServletRequest body) {
        var value = body.getParameter("value");
        var deadline = body.getParameter("deadline");
        var deadlineParsed = getLocalDateTime(deadline);
        logger.info("Form value: {}", value);
        var todo = new Todo.TodoBuilder().withContent(value)
                .withDeadLine(deadlineParsed)
                .build();

        var created = this.todoRepository.createTodo(todo);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("HX-Trigger", "newTodo");

        return new ResponseEntity<>(this.site.todoListElement(created), responseHeaders, HttpStatus.OK);
    }

    @PostMapping("/todos/done/{id}")
    public ResponseEntity<String> markDone(@PathVariable Long id) {
        var todo = this.todoRepository.markDone(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("HX-Trigger", "newTodo");

        return new ResponseEntity<>(this.site.todoString(todo), responseHeaders, HttpStatus.OK);
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
        this.todoRepository.deleteTodo(id);
        logger.info("deleting id {}", id);
        var headers = new HttpHeaders();
        headers.set("HX-Trigger", "deleteTodo");
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }
}
