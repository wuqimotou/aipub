package mt.aipub.config.model;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import mt.aipub.constant.ApiKeyConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingModelConfiguration {

    @Bean
    public EmbeddingModel qwenEmbeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(ApiKeyConstant.QWEN_API_KEY)
                .modelName("text-embedding-v3")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }


}
