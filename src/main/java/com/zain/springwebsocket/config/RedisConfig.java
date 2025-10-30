
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis 配置类
 * 配置 RedisTemplate 和消息监听容器
 */
@Configuration
public class RedisConfig {

    /**
     * Redis 消息监听容器
     * @param factory Redis 连接工厂
     * @param subscriber 自定义订阅器
     * @return RedisMessageListenerContainer
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory, RedisSubscriber subscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        // 订阅 chatChannel 频道
        container.addMessageListener(subscriber, new ChannelTopic("chatChannel"));
        return container;
    }

    /**
     * Redis 操作模板
     * @param factory Redis 连接工厂
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }
}
