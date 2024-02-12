package net.fredrikmeyer.logit.controllers;

import net.fredrikmeyer.logit.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class SimpleChatConfiguration implements WebSocketConfigurer {
    final Site site;
    private final StringRedisTemplate redisTemplate;

    public SimpleChatConfiguration(Site site, StringRedisTemplate redisTemplate) {
        this.site = site;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/chat")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

    @Autowired
    ChatControllers chatControllers;

    @Bean
    public WebSocketHandler myHandler() {
        return chatControllers;
    }
}
