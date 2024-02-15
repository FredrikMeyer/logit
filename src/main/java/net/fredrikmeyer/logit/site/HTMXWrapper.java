package net.fredrikmeyer.logit.site;

import j2html.tags.Tag;

public class HTMXWrapper {
    enum HXSWapAttribute {
        BeforeEnd {
            @Override
            public String toString() {
                return "beforeend";
            }
        },
        InnerHTML {
            @Override
            public String toString() {
                return "inner-html";
            }
        },
        None {
            @Override
            public String toString() {
                return "none";
            }
        }, OuterHTML {
            @Override
            public String toString() {
                return "outer-html";
            }
        }
    }
}
