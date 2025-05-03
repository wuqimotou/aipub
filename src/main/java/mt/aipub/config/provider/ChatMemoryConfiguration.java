package mt.aipub.config.provider;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.annotation.Resource;
import mt.aipub.store.WaiterChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfiguration {

    @Resource
    private WaiterChatMemoryStore waiterChatMemoryStore;

    @Bean
    public ChatMemoryProvider waiterChatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .chatMemoryStore(waiterChatMemoryStore)
                .id(memoryId)
                .maxMessages(50)
                .build();
    }

}
