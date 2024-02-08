package net.fredrikmeyer.logit;

import net.fredrikmeyer.logit.db.TodoDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogitApplication {
    @Autowired
    private TodoDAORepository todoDAORepository;

    public static void main(String[] args) {
        SpringApplication.run(LogitApplication.class, args);
    }

}