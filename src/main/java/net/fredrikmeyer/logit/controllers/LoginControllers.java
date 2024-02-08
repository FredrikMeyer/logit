package net.fredrikmeyer.logit.controllers;

import jakarta.servlet.http.HttpServletRequest;
import net.fredrikmeyer.logit.site.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController()
public class LoginControllers {
    Logger logger = LoggerFactory.getLogger(LoginControllers.class);
    @Autowired
    private Login login;

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        String tokenValue = Helpers.extractCsrfToken(request);

        var wasLoginError = Optional.ofNullable(request.getParameterValues("error"))
                .map(l -> l.length > 0)
                .orElse(false);

        String msg = null;
        if (wasLoginError) {
            msg = retrieveErrorMessage(request);
        }

        return login.root(tokenValue, msg);
    }

    private String retrieveErrorMessage(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) {
            var ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                return ex.getMessage();
            }
        }
        return null;
    }
}
