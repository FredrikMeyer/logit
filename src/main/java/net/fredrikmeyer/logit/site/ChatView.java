package net.fredrikmeyer.logit.site;

import j2html.tags.DomContent;
import org.springframework.stereotype.Component;

import static j2html.TagCreator.*;

@Component
public class ChatView {
    public DomContent chatWindow() {
        return article(header(h2("Chat")), div().withId("chatroom"),
                form(input().withName("chat-message")).withId("chat-send").attr("ws-send")

        )
                .attr("hx-ext", "ws")
                .attr("ws-connect", "/chat")
                .attr("hx-on:htmx:ws-after-send", "this.querySelector(\"form\").reset()");
    }

    public String chatResponse(String response) {
        return div(div(response))
                .attr("hx-swap-oob", HTMXWrapper.HXSWapAttribute.BeforeEnd)
                .withId("chatroom")
                .render();
    }
}
