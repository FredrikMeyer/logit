package net.fredrikmeyer.logit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public TodoRepository getTodoRepository() {
        return new TodoRepository();
    }
}
