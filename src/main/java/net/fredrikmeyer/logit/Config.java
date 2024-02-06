package net.fredrikmeyer.logit;

import net.fredrikmeyer.logit.db.TodoDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Autowired
    TodoDAORepository todoDAORepository;

    @Bean
    public TodoRepository getTodoRepository() {
        return new TodoRepository(todoDAORepository);
    }
}
