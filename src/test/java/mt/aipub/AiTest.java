package mt.aipub;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
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
    private ChatModel qwenChatModel;
    @Resource
    private ChatModel demoChatModel;
    @Resource
    private ChatMemoryProvider waiterChatMemoryProvider;
    @Resource Waiter waiter;


    @Test
    public void test() {
        System.out.println(waiter.chat("1", "你好"));
    }

}
