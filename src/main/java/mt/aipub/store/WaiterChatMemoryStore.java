package mt.aipub.store;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class WaiterChatMemoryStore implements ChatMemoryStore {

    private static final String MEMORY_KEY = "waiter:memory:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public WaiterChatMemoryStore() {

    }

    @Override
    public List<ChatMessage> getMessages(Object o) {
        String key = MEMORY_KEY + o.toString() ;
        String redisResult = redisTemplate.opsForValue().get(key);
        if (StringUtils.hasText(redisResult)) {
            List<ChatMessage> chatMessageList = ChatMessageDeserializer.messagesFromJson(redisResult);
            return chatMessageList;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {
        String key = MEMORY_KEY + o.toString() ;
        String memoryJson = ChatMessageSerializer.messagesToJson(list);
        redisTemplate.opsForValue().set(key, memoryJson, Duration.ofDays(1));
    }

    @Override
    public void deleteMessages(Object o) {
        String key = MEMORY_KEY + o.toString() ;
        redisTemplate.delete(key);
    }
}
