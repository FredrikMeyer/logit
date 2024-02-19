package net.fredrikmeyer.logit.site;

import j2html.attributes.Attribute;
import j2html.tags.Tag;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class HTMXWrapper {
    enum HXSWapAttribute {
        InnerHTML {
            @Override
            public String toString() {
                return "inner-html";
            }
        },
        OuterHTML {
            @Override
            public String toString() {
                return "outer-html";
            }
        },
        None {
            @Override
            public String toString() {
                return "none";
            }
        },
        Delete {
            @Override
            public String toString() {
                return "delete";
            }
        }, BeforeBegin {
            @Override
            public String toString() {
                return "beforebegin";
            }
        }, AfterBegin {
            @Override
            public String toString() {
                return "afterbegin";
            }
        }, BeforeEnd {
            @Override
            public String toString() {
                return "beforeend";
            }
        }, AfterEnd {
            @Override
            public String toString() {
                return "afterend";
            }
        }
    }

    static class AttributeBuilder {

        private final List<Attribute> attributes = new ArrayList<>();

        public AttributeBuilder swap(HXSWapAttribute attr) {
            this.attributes.add(new Attribute("hx-swap", attr.toString()));
            addAttribute("hx-swap", attr.toString());
            return this;
        }

        public AttributeBuilder confirm(String text) {
            addAttribute("hx-confirm", text);
            return this;
        }

        public AttributeBuilder delete(String url) {
            addAttribute("hx-delete", url);
            return this;
        }

        public AttributeBuilder post(String url) {
            addAttribute("hx-post", url);
            return this;
        }

        public AttributeBuilder get(String url) {
            addAttribute("hx-get", url);
            return this;
        }

        private void addAttribute(String attrName, String value) {
            this.attributes.add(new Attribute(attrName, value));
        }

        public AttributeBuilder target(String target) {
            addAttribute("hx-target", target);
            return this;
        }

        public <E extends Tag<T>, T extends Tag<T>> E build(Class<E> clazz) {
            try {
                E e = clazz.getConstructor()
                        .newInstance();

                attributes.forEach(e::attr);
                return e;

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }

        public <E extends Tag<T>, T extends Tag<T>> E build(E instance) {
            attributes.forEach(instance::attr);

            return instance;
        }
    }
}
