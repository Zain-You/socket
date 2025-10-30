import com.fasterxml.jackson.databind.ObjectMapper;
import com.zain.springwebsocket.handler.ChatWebSocketHandler;
import com.zain.springwebsocket.model.ChatMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;


/**
 * Redis 消息订阅器
 * 用于接收其他节点发布的消息，并广播到本节点客户端
 */
@Component
public class RedisSubscriber implements MessageListener {

    private final ChatWebSocketHandler handler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisSubscriber(ChatWebSocketHandler handler) {
        this.handler = handler;
    }

    /**
     * Redis 消息回调
     * @param message Redis 消息
     * @param pattern 订阅模式
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // 解析消息为 ChatMessage 对象
            ChatMessage chatMessage = objectMapper.readValue(message.getBody(), ChatMessage.class);
            // 广播到本节点客户端
            handler.broadcastMessage(chatMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
