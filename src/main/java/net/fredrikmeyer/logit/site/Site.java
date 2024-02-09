package net.fredrikmeyer.logit.site;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.UnescapedText;
import j2html.tags.specialized.SpanTag;
import net.fredrikmeyer.logit.Todo;
import net.fredrikmeyer.logit.site.HTMXWrapper.HXSWapAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

import static j2html.TagCreator.*;

@Component
public class Site {
    private final Layout layout;

    public Site(Layout layout) {
        this.layout = layout;
    }

    static Logger logger = LoggerFactory.getLogger(Site.class);


    public String root(String csrfToken) {
        return this.layout.root(getMainContent(), csrfToken);
    }


    private DomContent getMainContent() {
        return div(div(this.newTodoForm()),
                div(article(div("Loading...").attr("hx-get", "/todos")
                        .attr("hx-trigger", "load"))),
                div(article(chatWindow()).withClass("grid")));
    }

    private DomContent chatWindow() {
        return div(div().withId("chatroom"),
                form(input().withName("chat-message")).withId("chat-send")
                        .attr("ws-send")

        ).attr("hx-ext", "ws")
                .attr("ws-connect", "/chat")
                .attr("hx-on:htmx:ws-after-send", "this.querySelector(\"form\").reset()");
    }

    public String chatResponse(String response) {
        return div(response).attr("hx-swap-oob", HXSWapAttribute.BeforeEnd.toString())
                .withId("chatroom")
                .render();
    }

    private DomContent newTodoForm() {
        return article(form(label("Fill in todo").withFor("value"),
                input().withType("text")
                        .withId("value")
                        .attr("required")
                        .withName("value"),
                label("Deadline").withFor("deadline"),
                input().withType("date")
                        .withName("deadline")
                        .withId("deadline"),
                button("Submit")).attr("hx-post", "/todo")
                .attr("hx-swap", "beforeend")
                .attr("hx-target", "#todo-list")
                .attr("hx-on::after-request", "this.reset()"));
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
