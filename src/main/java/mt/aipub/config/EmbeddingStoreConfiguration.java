package mt.aipub.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeServerlessIndexConfig;
import jakarta.annotation.Resource;
import mt.aipub.constant.ApiKeyConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingStoreConfiguration {

    @Resource
    private OpenAiEmbeddingModel qwenEmbeddingModel;

    @Bean
    public EmbeddingStore<TextSegment> pineconeEmbeddingStore() {
        return PineconeEmbeddingStore.builder()
                .apiKey(ApiKeyConstant.PINECONE_API_KEY)
                .index("aipub-index")
                .nameSpace("aipub-namespace")
                .createIndex(PineconeServerlessIndexConfig.builder()
                        .cloud("AWS")
                        .dimension(qwenEmbeddingModel.dimension())
                        .region("us-east-1")
                        .build())
                .build();
    }
}
