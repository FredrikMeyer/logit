package net.fredrikmeyer.logit;

import net.fredrikmeyer.logit.controllers.ChatControllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageReceiver {
    @Autowired
    private ChatControllers chatControllers;

    Logger logger = LoggerFactory.getLogger(ChatMessageReceiver.class);

    @SuppressWarnings("unused")
    public void receiveMessage(String message) {
        try {
            chatControllers.broadcast(message);
        } catch (Exception e) {
            logger.error("Exception receiving message.", e);
        }
    }
}
