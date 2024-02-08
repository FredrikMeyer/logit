package net.fredrikmeyer.logit.controllers;

import jakarta.servlet.http.HttpServletRequest;
import net.fredrikmeyer.logit.site.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class LoginControllers {
    Logger logger = LoggerFactory.getLogger(LoginControllers.class);
    @Autowired
    private Login login;

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        String tokenValue = Helpers.extractCsrfToken(request);

        return login.root(tokenValue);
    }
}
