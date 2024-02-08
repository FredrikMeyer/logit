package net.fredrikmeyer.logit.site;

import j2html.tags.DomContent;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static j2html.TagCreator.*;

@Component
public class Login {
    private final Layout layout;

    public Login(Layout layout) {
        this.layout = layout;
    }

    public String root(String tokenValue, String loginError) {
        return this.layout.root(div(div(loginForm(tokenValue, loginError))).withClass("grid"), tokenValue);
    }

    private DomContent loginForm(String tokenValue, String isLoginError) {
        return article(iff(!Objects.isNull(isLoginError), loginError(isLoginError)),
                form(label("User Name").withFor("username"),
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

    private DomContent loginError(String isLoginError) {
        return p(strong("Login error: " + isLoginError).withClass("error"));
    }
}
