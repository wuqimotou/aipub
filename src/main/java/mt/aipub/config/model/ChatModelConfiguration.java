package mt.aipub.config.model;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static mt.aipub.constant.ApiKeyConstant.QWEN_API_KEY;

@Configuration
public class ChatModelConfiguration {
    ;
    @Bean
    public ChatModel demoChatModel() {
        return OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public ChatModel qwenChatModel() {
        return OpenAiChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(QWEN_API_KEY)
                .modelName("qwen-plus-latest")
                .build();
    }

}
