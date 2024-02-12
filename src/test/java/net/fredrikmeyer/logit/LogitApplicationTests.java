package net.fredrikmeyer.logit;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {LogitApplication.class, TestConfiguration.class})
class LogitApplicationTests {

    @Test
    void contextLoads() {
    }

}
