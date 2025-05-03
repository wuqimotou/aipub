package mt.aipub;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import mt.aipub.bot.Waiter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class AiTest {

    @Resource
    private ChatLanguageModel qwenChatModel;
    @Resource
    private ChatLanguageModel demoChatModel;
    @Resource
    private ChatMemoryProvider waiterChatMemoryProvider;


    @Test
    public void test() {
        Waiter waiter = AiServices.builder(Waiter.class)
                .chatLanguageModel(demoChatModel)
                .chatMemoryProvider(waiterChatMemoryProvider)
                .build();
        System.out.println(waiter.chat("1", "你好"));

    }

}
