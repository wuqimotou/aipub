package mt.aipub.job;

import com.alibaba.dashscope.tokenizers.QwenTokenizer;
import com.xxl.job.core.handler.annotation.XxlJob;
import dev.langchain4j.community.model.dashscope.QwenTokenCountEstimator;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import mt.aipub.constant.ApiKeyConstant;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RefreshRAGDocumentsJobHandle {
    @Resource
    private OpenAiEmbeddingModel qwenEmbeddingModel;
    @Resource
    private EmbeddingStore<TextSegment> pineconeEmbeddingStore;

    @XxlJob("RefreshRAGDocuments")
    public void autoGlobalUpdateMemory(String memoryId) {
        // 获取所有文件
        List<Document> documents = ClassPathDocumentLoader.loadDocumentsRecursively("documents", new ApacheTikaDocumentParser());
        System.out.println(documents.get(0));
        // 将文件向量化并存入向量数据库
        EmbeddingStoreIngestor.builder()
                .documentSplitter(new DocumentByParagraphSplitter(1024, 0, new QwenTokenCountEstimator(ApiKeyConstant.QWEN_API_KEY, "qwen-plus-latest")))
                .embeddingModel(qwenEmbeddingModel)
                .embeddingStore(pineconeEmbeddingStore)
                .build()
                .ingest(documents);
    }
}
