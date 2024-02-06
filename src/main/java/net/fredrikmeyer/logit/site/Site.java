package net.fredrikmeyer.logit.site;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.specialized.BodyTag;
import j2html.tags.specialized.SpanTag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.fredrikmeyer.logit.Resources;
import net.fredrikmeyer.logit.Todo;
import net.fredrikmeyer.logit.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;

import static j2html.TagCreator.*;

@RestController("/")
public class Site {
    private final TodoRepository todoRepository;

    public Site(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    static Logger logger = LoggerFactory.getLogger(Site.class);

    @GetMapping("/")
    public String root() {
        var picoCssVersion = Resources.readVersion("picocss");
        logger.info("Pico  version " + picoCssVersion);
        String rendered = html(head(link().attr("rel", "stylesheet")
                                .withHref("/webjars/picocss__pico/" + picoCssVersion + "/css/pico.min.css"),
                        link().attr("rel", "stylesheet")
                                .withHref("/custom.css")),
                meta().attr("charset", "utf-8"),
                meta().withName("viewport")
                        .attr("value", "width=device-width, initial-scale=1"),
                title("First todo app"),
                getBody()).render();

        return "<!DOCTYPE html>\n" + rendered;
    }


    private BodyTag getBody() {
        var htmxVersion = Resources.readVersion("htmx");

        logger.debug("Htmx version {}", htmxVersion);

        return body(nav(ul(li(strong("ToDo"))), ul(li("Teeny tiny"))).withClass("container-fluid"),
                main(div(div(this.newTodoForm()),
                        div(article(div("Loading...").attr("hx-get", "/todos")
                                .attr("hx-trigger", "load")))).withClass("grid")).withClass("container"),

                script().withSrc("/webjars/htmx.org/" + htmxVersion + "/dist/htmx.min.js"));
    }

    private DomContent newTodoForm() {
        return article(form(label("Fill in todo").withFor("value"),
                input().withType("text")
                        .withId("value")
                        .attr("required")
                        .withName("value"),
                button("Submit")).attr("hx-post", "/todo")
                .attr("hx-swap", "beforeend")
                .attr("hx-target", "#todo-list"));
    }

    @RequestMapping("/test2")
    public String test2(HttpSession session) {
        Integer counter = (Integer) session.getAttribute("counter");
        if (counter == null) {
            counter = 0;
        }
        // Increment the counter
        counter++;
        // Store the updated counter back in the session
        session.setAttribute("counter", counter);

        var content = "You clicked " + counter + " times";

        return div().withText(content)
                .render();
    }

    @RequestMapping("/todos")
    public String getTodos() {
        var todos = this.todoRepository.listTodos();

        return div(this.todoSummary(),
                ul(each(todos.stream()
                        .sorted(Comparator.comparing(t -> t.created))
                        .toList(), todo -> li(todoHtml(todo)))).withId("todo-list")).render();
    }

    private ContainerTag<SpanTag> todoSummary() {
        return span(todoSummaryText()).withId("todo-summary")
                .attr("hx-trigger", "newTodo from:body")
                .attr("hx-get", "/todos/summary");
    }

    @GetMapping("/todos/summary")
    public String todoSummaryUpdated() {
        return this.todoSummary()
                .render();
    }

    private String todoSummaryText() {
        var total = this.todoRepository.numberOfTodos();
        var done = this.todoRepository.numberOfDone();
        return "Number of todos: " + total + ". Number done: " + done + ".";
    }

    @PostMapping("/todo")
    public ResponseEntity<String> newTodo(HttpServletRequest body) {
        var value = body.getParameter("value");

        logger.info("Form value: {}", value);

        var todo = new Todo(value);
        var created = this.todoRepository.createTodo(todo);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("HX-Trigger", "newTodo");

        return new ResponseEntity<>(li(todoHtml(created)).render(), responseHeaders, HttpStatus.OK);
    }

    private DomContent todoHtml(Todo todo) {
        var deleteButton = a("X").attr("role", "button")
                .withHref("#")
                .attr("hx-delete", "/todo/" + todo.id)
                .attr("hx-swap", "delete")
                .attr("hx-target", "closest li")
                .attr("hx-confirm", "Really delete?")
                .withClass("outline");
        var base = todo.humanString();

        return join(span(todo.done ? s(base) : text(base)).attr("hx-swap", "innerHTML")
                .attr("hx-post", "/todos/done/" + todo.id)
                .attr("hx-target", "closest li"), deleteButton);
    }

    @PostMapping("/todos/done/{id}")
    public String markDone(@PathVariable Long id) {
        var todo = this.todoRepository.markDone(id);

        var summary = this.todoSummary()
                .withId("todo-summary")
                .attr("hx-swap-oob", "true");

        return todoHtml(todo).render() + summary.render();
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
        this.todoRepository.deleteTodo(id);
        logger.info("deleting id {}", id);
        var headers = new HttpHeaders();
        headers.set("HX-Trigger", "newTodo");
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }
}
