package net.fredrikmeyer.logit.site;

import j2html.tags.Tag;

public class HTMXWrapper {
    public enum Triggers {
        A, B, C
    }

    public static <E extends Tag<E>> Tag<E> withTrigger(Tag<E> tag, Triggers trigger) {
        return tag.attr("g", "ddd");
    }
}
