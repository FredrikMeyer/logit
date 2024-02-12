package net.fredrikmeyer.logit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    public int redisPort;
    public String redisHost;

    public RedisConfig(
        @Value("${spring.data.redis.port}") int redisPort,
        @Value("${spring.data.redis.host}") String redisHost
    ) {
        this.redisPort = redisPort;
        this.redisHost = redisHost;
    }
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        RedisMessageListenerContainer o = new RedisMessageListenerContainer();

        return template;
    }

    @Bean
    RedisMessageListenerContainer container(MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListenerAdapter, topic());

        return container;
    }

    @Bean
    MessageListenerAdapter messageListener(ChatMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    ChatMessageReceiver receiver() {
        return new ChatMessageReceiver();
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("chat");
    }
}
