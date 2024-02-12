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

    public void receiveMessage(String message) {
        logger.info("I'm called... " + message);
        try {
            chatControllers.broadcast(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
