package net.fredrikmeyer.logit.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Optional;

public class Helpers {
    static Logger logger = LoggerFactory.getLogger(Helpers.class);

    public static String extractCsrfToken(HttpServletRequest request) {
        Optional<CsrfToken> csrfToken = Optional.ofNullable((CsrfToken) request.getAttribute(CsrfToken.class.getName()));
        logger.info("request {}", csrfToken.get());

        return csrfToken.map(CsrfToken::getToken)
                .orElse("");
    }

    public static LocalDateTime getLocalDateTime(String deadline) {
        var formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .toFormatter();

        var deadlineParsed = (deadline != null && !deadline.isEmpty()) ? LocalDateTime.parse(deadline,
                formatter) : null;
        return deadlineParsed;
    }
}
