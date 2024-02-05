package net.fredrikmeyer.logit.site;

import net.fredrikmeyer.logit.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SiteTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;

    @Test
    void newTodo() {
        var site = new Site(this.todoRepository);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("value", "my new todo");

        var entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        var res = testRestTemplate.postForEntity(String.format("http://localhost:%s/todo", this.port),
                entity, String.class);

        assertEquals(res.getBody(), "dsads");
    }
}