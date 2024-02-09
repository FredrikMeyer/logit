package net.fredrikmeyer.logit.site;

import j2html.tags.DomContent;
import net.fredrikmeyer.logit.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static j2html.TagCreator.*;

@Component
public class Layout {
    Logger logger = LoggerFactory.getLogger(Layout.class);

    public Layout() {
    }

    public String root(DomContent mainContent, String csrfToken) {
        var htmxVersion = Resources.readVersion("htmx");

        logger.debug("HTMX version {}", htmxVersion);

        String rendered = html(getHead(),
                body(getNav(), main(mainContent).withClass("container")).attr("hx-headers",
                        String.format("{\"X-CSRF-Token\": \"%s\"}", csrfToken)),
                script().withSrc("/webjars/htmx.org/" + htmxVersion + "/dist/htmx.min.js"),
                script().withSrc("/webjars/htmx.org/" + htmxVersion + "/dist/ext/ws.js")).render();

        return "<!DOCTYPE html>\n" + rendered;
    }

    private DomContent getHead() {
        var picoCssVersion = Resources.readVersion("picocss");
        logger.debug("Pico  version {}", picoCssVersion);

        return head(link().attr("rel", "stylesheet")
                        .withHref("/webjars/picocss__pico/" + picoCssVersion + "/css/pico.min.css"),
                link().attr("rel", "stylesheet")
                        .withHref("/custom.css"),
                meta().attr("charset", "utf-8"),
                meta().withName("viewport")
                        .attr("value", "width=device-width, initial-scale=1"),
                title("First todo app"));
    }

    private DomContent getNav() {
        return nav(ul(li(strong("ToDo"))), ul(li("Teeny tiny"))).withClass("container-fluid");
    }
}
