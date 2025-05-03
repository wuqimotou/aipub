package mt.aipub.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import mt.aipub.bot.Waiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

    @Resource
    private ChatLanguageModel qwenChatModel;
    @Resource
    private ChatMemoryProvider waiterChatMemoryProvider;

    @Bean
    public Waiter waiter() {
        return AiServices.builder(Waiter.class)
                .chatMemoryProvider(waiterChatMemoryProvider)
                .chatLanguageModel(qwenChatModel)
                .build();
    }

}
