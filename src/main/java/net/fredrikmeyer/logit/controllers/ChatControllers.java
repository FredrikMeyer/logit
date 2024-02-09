package net.fredrikmeyer.logit.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fredrikmeyer.logit.site.Site;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatControllers extends TextWebSocketHandler {
    private final List<WebSocketSession> activeSession = new CopyOnWriteArrayList<>();
    private final Site site;

    public ChatControllers(Site site) {
        this.site = site;
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
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(message.getPayload()
                .toString());

        String msg = rootNode.path("chat-message")
                .asText();

        String user = null;
        Principal principal = session.getPrincipal();
        if (principal != null) {
            user = principal.getName();
        }

        String result = user + "> " + msg;

        broadcast(result);
    }

    public void broadcast(String message) throws IOException {
        for (WebSocketSession session : activeSession) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(site.chatResponse(message)));
            }
        }
    }
}
