package net.fredrikmeyer.logit.site;

import j2html.tags.specialized.ATag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTMXWrapperTest {
    @Test
    public void canCreateTag() {
        assertDoesNotThrow(() -> new HTMXWrapper.AttributeBuilder().build(ATag.class));
    }

}