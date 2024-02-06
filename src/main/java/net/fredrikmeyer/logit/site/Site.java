package net.fredrikmeyer.logit.site;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.UnescapedText;
import j2html.tags.specialized.BodyTag;
import j2html.tags.specialized.SpanTag;
import net.fredrikmeyer.logit.Resources;
import net.fredrikmeyer.logit.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

import static j2html.TagCreator.*;

@Component
public class Site {

    public Site() {
    }

    static Logger logger = LoggerFactory.getLogger(Site.class);


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


    public String getTodos(List<Todo> todos, long numberDone) {
        var total = todos.size();
        return div(this.todoSummary(total, numberDone),
                ul(each(todos.stream()
                        .sorted(Comparator.comparing(t -> t.created))
                        .toList(), todo -> li(todoHtml(todo)))).withId("todo-list")).render();
    }

    private ContainerTag<SpanTag> todoSummary(long total, long done) {
        return span(todoSummaryText(total, done)).withId("todo-summary")
                .attr("hx-trigger", "newTodo from:body, deleteTodo from:body")
                .attr("hx-get", "/todos/summary");
    }

    public String todoSummaryUpdated(long total, long done) {
        return this.todoSummary(total, done)
                .render();
    }

    private String todoSummaryText(long total, long done) {
        return "Number of todos: " + total + ". Number done: " + done + ".";
    }

    public String todoString(Todo todo) {
        return todoHtml(todo).render();
    }

    public String todoListElement(Todo todo) {
        return li(todoHtml(todo)).render();
    }

    private UnescapedText todoHtml(Todo todo) {
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
}
