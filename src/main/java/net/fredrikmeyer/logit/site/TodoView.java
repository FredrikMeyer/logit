package net.fredrikmeyer.logit.site;

import net.fredrikmeyer.logit.Todo;

import static j2html.TagCreator.*;

public class TodoView {
    public String todoExpanded(Todo todo) {
        return article(header(todo.content), div("body")).withId("todo-" + todo.id)
                .attr("hx-swap-oob", true)
                .render();
    }
}
