package net.fredrikmeyer.logit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fredrikmeyer.logit.site.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatControllers extends TextWebSocketHandler {
    private final List<WebSocketSession> activeSession = new CopyOnWriteArrayList<>();
    private final Site site;

    Logger logger = LoggerFactory.getLogger(ChatControllers.class);

    private final StringRedisTemplate redisTemplate;

    public ChatControllers(Site site, StringRedisTemplate redisTemplate) {
        this.site = site;
        this.redisTemplate = redisTemplate;
        logger.info("I WAS CREATED");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        activeSession.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        activeSession.remove(session);
        logger.info("Removed session " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        String msg = getMessageText(message);

        String user = null;
        Principal principal = session.getPrincipal();
        if (principal != null) {
            user = principal.getName();
        }

        String result = user + "> " + msg;
        redisTemplate.convertAndSend("chat", result);
    }

    private static String getMessageText(WebSocketMessage<?> message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(message.getPayload()
                .toString());

        return rootNode.path("chat-message")
                .asText();
    }

    public void broadcast(String message) throws IOException {
        for (WebSocketSession session : activeSession) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(site.chatResponse(message)));
            }
        }
    }
}
