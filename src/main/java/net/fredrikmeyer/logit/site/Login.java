package net.fredrikmeyer.logit.site;

import j2html.tags.DomContent;
import org.springframework.stereotype.Component;

import static j2html.TagCreator.*;

@Component
public class Login {
    private final Layout layout;

    public Login(Layout layout) {
        this.layout = layout;
    }

    public String root(String tokenValue) {
        return this.layout.root(div(div(loginForm(tokenValue))).withClass("grid"), tokenValue);
    }

    private DomContent loginForm(String tokenValue) {
        return article(form(label("User Name").withFor("username"),
                input().withType("hidden")
                        .withName("_csrf")
                        .withValue(tokenValue),
                input().withName("username")
                        .withId("username")
                        .attr("autocomplete", "username"),
                input().withType("password")
                        .withName("password"),
                button("Log in").withType("submit")).withAction(
                        "/login")
                .withMethod("post")
                .withAction("/login"));
    }
}
