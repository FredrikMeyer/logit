package net.fredrikmeyer.logit.site;

import j2html.tags.DomContent;
import j2html.tags.specialized.BodyTag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.fredrikmeyer.logit.Resources;
import net.fredrikmeyer.logit.Todo;
import net.fredrikmeyer.logit.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static j2html.TagCreator.*;

@RestController("/")
public class Site {
    TodoRepository todoRepository;

    public Site(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    static Logger logger = LoggerFactory.getLogger(Site.class);

    @GetMapping("/")
    public String root() {
        var picoCssVersion = Resources.readVersion("picocss");
        logger.info("Pico version " + picoCssVersion);
        String rendered = html(head(link().attr("rel", "stylesheet")
                        .withHref("/webjars/picocss__pico/" + picoCssVersion + "/css/pico.min.css")),
                meta().attr("charset", "utf-8"),
                meta().withName("viewport").attr("value", "width=device-width, initial-scale=1"),
                title("First todo app"),
                getBody()).render();
        return "<!DOCTYPE html>\n" + rendered;
    }


    private BodyTag getBody() {
        var htmxVersion = Resources.readVersion("htmx");

        logger.debug("Htmx version {}", htmxVersion);

        return body(main(h1("Simple ToDO"),
                        div(div(this.newTodoForm()),
                                div(article(div("Loading...").attr("hx-get", "/todos")
                                        .attr("hx-trigger", "load"))))
                                .withClass("grid")
                ).withClass("container"),

                script().withSrc("/webjars/htmx.org/" + htmxVersion + "/dist/htmx.min.js"));
    }

    private DomContent newTodoForm() {
        return article(form(label("Fill in todo").withFor("value"),
                input().withType("text")
                        .withId("value")
                        .attr("required")
                        .withName("value"), button("Submit"))
                .attr("hx-post", "/todo")
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

        return div().withText(content).render();
    }

    @RequestMapping("/todos")
    public String getTodos() {
        var todos = this.todoRepository.listTodos();

        return div(text("Number of todos: " + todos.size()), ul(each(todos, todo -> li(todoHtml(todo))))
                .withId("todo-list"))
                .render();
    }

    @PostMapping("/todo")
    public String newTodo(HttpServletRequest body) {
        var value = body.getParameter("value");

        logger.info("Form value: ", value);

        var todo = new Todo(value);
        this.todoRepository.createTodo(todo);

        return li(todoHtml(todo)).render();
    }

    private DomContent todoHtml(Todo todo) {
        var deleteButton = a("X")
                .attr("role", "button")
                .withHref("#")
                .attr("hx-delete", "/todo/" + todo.id)
                .attr("hx-swap", "delete")
                .attr("hx-target", "closest li")
                .attr("hx-confirm", "Really delete?")
                .withClass("outline");
        var base = todo.humanString();

        return join(span(todo.done ? s(base) : text(base))
                        .attr("hx-post", "/todos/done/" + todo.id)
                        .attr("hx-swap", "innerHTML")
                        .attr("hx-target", "closest li"),
                deleteButton);
    }

    @PostMapping("/todos/done/{id}")
    public String markDone(@PathVariable String id) {
        var todo = this.todoRepository.markDone(id);

        return todoHtml(todo).render();
    }

    @DeleteMapping("/todo/{id}")
    public String deleteTodo(@PathVariable String id) {
        this.todoRepository.deleteTodo(id);
        logger.info("deleting");
        return "...";
    }
}
