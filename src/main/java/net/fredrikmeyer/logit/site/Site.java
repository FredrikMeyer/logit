package net.fredrikmeyer.logit.site;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.Tag;
import j2html.tags.specialized.ATag;
import j2html.tags.specialized.DivTag;
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
    private final ChatView chatView;

    public Site(Layout layout, ChatView chatView) {
        this.layout = layout;
        this.chatView = chatView;
    }

    static Logger logger = LoggerFactory.getLogger(Site.class);


    public String root(String csrfToken) {
        return this.layout.root(getMainContent(), csrfToken);
    }


    private DomContent getMainContent() {
        return div(div(this.newTodoForm()), div(article(header(h2("All todos")),
                div(span("Loading...").attr("aria-busy", "true"))
                        .attr("hx-get", "/todos")
                        .attr("hx-swap", HXSWapAttribute.OuterHTML)
                        .attr("hx-trigger", "load"))), div(chatView.chatWindow())).withClass("grid");
    }

    private DomContent newTodoForm() {
        return article(header(h2("New todo")), form(label("Fill in todo").withFor("value"),
                input().withType("text").withId("value").attr("required").withName("value"),
                label("Deadline").withFor("deadline"), input().withType("date").withName("deadline").withId("deadline"),
                button("Submit"))
                .attr("hx-post", "/todo")
                .attr("hx-swap", "beforeend")
                .attr("hx-target", "#todo-list")
                .attr("hx-on::after-request", "this.reset()"));
    }


    public String getTodos(List<Todo> todos, long numberDone) {
        var total = todos.size();
        return div(this.todoSummary(total, numberDone),
                ul(each(todos.stream().sorted(Comparator.comparing(t -> t.created)).toList(),
                        todo -> li(todoHtml(todo)))).withId("todo-list")).render();
    }

    private ContainerTag<SpanTag> todoSummary(long total, long done) {
        return span(todoSummaryText(total, done))
                .withId("todo-summary")
                .attr("hx-trigger", "newTodo from:body, deleteTodo from:body")
                .attr("hx-get", "/todos/summary");
    }

    public String todoSummaryUpdated(long total, long done) {
        return this.todoSummary(total, done).render();
    }

    private String todoSummaryText(long total, long done) {
        return "Number of todos: " + total + ". Number done: " + done + ".";
    }

    public String todoString(Todo todo) {
        return todoHtml(todo).render();
    }

    public String todoString(Todo todo, boolean expanded) {
        return todoHtml(todo, expanded).render();
    }

    public String todoListElement(Todo todo) {
        return li(todoHtml(todo)).render();
    }

    private DomContent todoHtml(Todo todo) {
        return todoHtml(todo, false);
    }

    private DomContent todoHtml(Todo todo, boolean expanded) {
        var deleteButton = new HTMXWrapper.AttributeBuilder()
                .swap(HXSWapAttribute.Delete)
                .delete("/todo/" + todo.id)
                .confirm("Really delete?")
                .target("closest li")
                .build(a("✖"))
                .withHref("#")
                .attr("role", "button")
                .withClass("outline");

        var markDoneButton = new HTMXWrapper.AttributeBuilder()
                .post("/todos/done/" + todo.id)
                .target("closest li")
                .swap(HXSWapAttribute.InnerHTML)
                .build(a("✔"))
                .attr("role", "button")
                .withHref("#")
                .withClass("outline");

        var base = todo.humanString();

        DomContent todoText = new HTMXWrapper.AttributeBuilder()
                .get(String.format("/todos/%s?expanded=%s", todo.id, expanded))
                .target("#todo-" + todo.id)
                .build(div(todo.done ? s(base) : text(base)));

        return div(div(todoText, div(markDoneButton, deleteButton).withClass("button-group")).withClass("todo-header"))
                .withClass("todo")
                .withId("todo-" + todo.id);
    }
}
