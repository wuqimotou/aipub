package mt.aipub;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenTokenCountEstimator;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import mt.aipub.bot.Waiter;
import mt.aipub.constant.ApiKeyConstant;
import okhttp3.sse.EventSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class AiTest {

    @Resource
    private ChatModel qwenChatModel;
    @Resource
    private ChatModel demoChatModel;
    @Resource
    private ChatMemoryProvider waiterChatMemoryProvider;
    @Resource
    Waiter waiter;
    @Resource
    private EmbeddingStore<TextSegment> pineconeEmbeddingStore;
    @Resource
    private OpenAiEmbeddingModel qwenEmbeddingModel;



    @Test
    public void test() {
        List<Document> document = ClassPathDocumentLoader.loadDocumentsRecursively("documents");
        DocumentByParagraphSplitter documentByParagraphSplitter = new DocumentByParagraphSplitter(1024, 0, new QwenTokenCountEstimator(ApiKeyConstant.QWEN_API_KEY, "qwen-plus-latest"));
        List<TextSegment> textSegments = documentByParagraphSplitter.split(document.get(0));

        textSegments.forEach(textSegment -> {
            System.out.println("-----");
            System.out.println(textSegment.text());
        });
    }



}
