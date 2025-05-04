package mt.aipub.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import mt.aipub.bot.Waiter;
import mt.aipub.bot.impl.WaiterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

    @Resource
    private ChatModel qwenChatModel;
    @Resource
    private ChatMemoryProvider waiterChatMemoryProvider;

    @Bean
    public Waiter waiter() {
        return new WaiterImpl(waiterChatMemoryProvider, qwenChatModel);
    }

}
